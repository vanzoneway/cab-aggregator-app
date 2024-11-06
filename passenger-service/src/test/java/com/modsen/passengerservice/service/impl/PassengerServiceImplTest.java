package com.modsen.passengerservice.service.impl;

import com.modsen.passengerservice.AppTestUtil;
import com.modsen.passengerservice.dto.ListContainerResponseDto;
import com.modsen.passengerservice.dto.PassengerDto;
import com.modsen.passengerservice.exception.passenger.DuplicatePassengerPhoneOrEmailException;
import com.modsen.passengerservice.exception.passenger.PassengerNotFoundException;
import com.modsen.passengerservice.mapper.ListContainerMapper;
import com.modsen.passengerservice.mapper.PassengerMapper;
import com.modsen.passengerservice.model.Passenger;
import com.modsen.passengerservice.repository.PassengerRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class PassengerServiceImplTest {

    @Mock
    private ListContainerMapper listContainerMapper;

    @Mock
    private MessageSource messageSource;

    @Mock
    private PassengerMapper passengerMapper;

    @Mock
    private PassengerRepository passengerRepository;

    @InjectMocks
    private PassengerServiceImpl passengerServiceImpl;

    @Test
    @DisplayName("Test createPassenger(PassengerDto); then call checkPassengerExistsByPhoneOrEmail and " +
            "throw DuplicatePassengerPhoneOrEmailException")
    void testCreatePassenger_thenCallCheckPassengerExistsByPhoneOrEmail() {
        // Arrange
        when(passengerRepository.existsByEmailAndDeletedIsFalse(AppTestUtil.passenger.getEmail()))
                .thenReturn(true);
        when(messageSource.getMessage(any(), any(Object[].class), any(Locale.class)))
                .thenReturn("");

        // Act and Assert
        assertThatThrownBy(() -> passengerServiceImpl.createPassenger(AppTestUtil.passengerDto))
                .isInstanceOf(DuplicatePassengerPhoneOrEmailException.class);

        verify(passengerRepository).existsByEmailAndDeletedIsFalse(AppTestUtil.passenger.getEmail());
    }

    @Test
    @DisplayName("Test createPassenger(PassengerDto); then call checkPassengerRestoreOption and " +
            "throw DuplicatePassengerPhoneOrEmailException")
    void testCreatePassenger_thenCallCheckPassengerRestoreOption() {
        // Arrange
        when(passengerRepository.existsByEmailAndDeletedIsFalse(AppTestUtil.passenger.getEmail()))
                .thenReturn(false);
        when(passengerRepository.existsByEmailAndDeletedIsTrue(AppTestUtil.passenger.getEmail()))
                .thenReturn(true);
        when(messageSource.getMessage(any(), any(Object[].class), any(Locale.class)))
                .thenReturn("");

        // Act and Assert
        assertThatThrownBy(() -> passengerServiceImpl.createPassenger(AppTestUtil.passengerDto))
                .isInstanceOf(DuplicatePassengerPhoneOrEmailException.class);

        verify(passengerRepository).existsByEmailAndDeletedIsTrue(AppTestUtil.passenger.getEmail());
    }

    @Test
    @DisplayName("Test createPassenger(PassengerDto); then success")
    void testCreatePassenger_thenSuccess() {
        // Arrange
        when(passengerRepository.existsByEmailAndDeletedIsFalse(AppTestUtil.passenger.getEmail()))
                .thenReturn(false);
        when(passengerRepository.existsByEmailAndDeletedIsTrue(AppTestUtil.passenger.getEmail()))
                .thenReturn(false);
        when(passengerMapper.toEntity(any(PassengerDto.class)))
                .thenReturn(AppTestUtil.passenger);
        when(passengerRepository.save(any(Passenger.class)))
                .thenReturn(AppTestUtil.passenger);
        when(passengerMapper.toDto(any(Passenger.class)))
                .thenReturn(AppTestUtil.passengerDto);

        // Act
        PassengerDto actual = passengerServiceImpl.createPassenger(AppTestUtil.passengerDto);

        // Assert
        assertThat(actual)
                .isNotNull()
                .isEqualTo(AppTestUtil.passengerDto);

        verify(passengerRepository).save(any(Passenger.class));
    }

    @Test
    @DisplayName("Test getPagePassengers; then success")
    void testGetPagePassengers_thenSuccess() {
        // Arrange
        int offset = 1;
        int limit = 10;
        ListContainerResponseDto<PassengerDto> expectedResponse = ListContainerResponseDto.<PassengerDto>builder()
                .withTotalElements(1)
                .withTotalPages(1)
                .withValues(Collections.singletonList(AppTestUtil.passengerDto))
                .build();
        List<Passenger> passengers = Collections.singletonList(AppTestUtil.passenger);
        Page<Passenger> passengerPage = new PageImpl<>(passengers);

        when(passengerRepository.findAllByDeletedIsFalse(PageRequest.of(offset, limit)))
                .thenReturn(passengerPage);
        when(passengerMapper.toDto(any(Passenger.class)))
                .thenReturn(AppTestUtil.passengerDto);
        when(listContainerMapper.toDto(any(Page.class)))
                .thenReturn(expectedResponse);

        // Act
        ListContainerResponseDto<PassengerDto> actual = passengerServiceImpl.getPagePassengers(offset, limit);

        // Assert
        assertThat(actual)
                .isNotNull()
                .extracting(ListContainerResponseDto::totalPages, ListContainerResponseDto::totalElements,
                        r -> r.values().getFirst())
                .containsExactly(1, 1L, AppTestUtil.passengerDto);

        verify(passengerRepository).findAllByDeletedIsFalse(PageRequest.of(offset, limit));
        verify(passengerMapper).toDto(any(Passenger.class));
    }

    @Test
    @DisplayName("Test safeDeletePassengerById(Long); then call getPassengerByIdAndDeletedIsFalse(Long)")
    void testSafeDeletePassengerById_thenCallGetPassengerByIdAndDeletedIsFalse() {
        // Arrange
        Long passengerId = 1L;
        when(passengerRepository.findByIdAndDeletedIsFalse(any(Long.class)))
                .thenReturn(Optional.empty());
        when(messageSource.getMessage(any(), any(Object[].class), any(Locale.class)))
                .thenThrow(PassengerNotFoundException.class);

        // Act and Assert
        assertThatThrownBy(() -> passengerServiceImpl.safeDeletePassengerById(passengerId))
                .isInstanceOf(PassengerNotFoundException.class);
    }

    @Test
    @DisplayName("Test safeDeletePassengerById(Long); then success")
    void testSafeDeletePassengerById_thenSuccess() {
        // Arrange
        Long passengerId = 1L;
        when(passengerRepository.findByIdAndDeletedIsFalse(any(Long.class)))
                .thenReturn(Optional.of(AppTestUtil.passenger));
        when(passengerRepository.save(any(Passenger.class)))
                .thenReturn(AppTestUtil.passenger);

        // Act
        passengerServiceImpl.safeDeletePassengerById(passengerId);

        // Assert
        verify(passengerRepository).save(any(Passenger.class));
        verify(passengerRepository).findByIdAndDeletedIsFalse(passengerId);
    }

    @Test
    @DisplayName("Test updatePassengerById; then success")
    void testUpdatePassengerById_thenSuccess() {
        Long passengerId = 1L;
        when(passengerRepository.findByIdAndDeletedIsFalse(any(Long.class)))
                .thenReturn(Optional.of(AppTestUtil.passenger));
        doNothing()
                .when(passengerMapper).partialUpdate(any(PassengerDto.class), any(Passenger.class));
        when(passengerRepository.save(any(Passenger.class)))
                .thenReturn(AppTestUtil.passenger);
        when(passengerMapper.toDto(any(Passenger.class)))
                .thenReturn(AppTestUtil.passengerDto);

        // Act
        passengerServiceImpl.updatePassengerById(passengerId, AppTestUtil.passengerDto);

        // Assert
        verify(passengerRepository).save(any(Passenger.class));
        verify(passengerRepository).findByIdAndDeletedIsFalse(passengerId);
    }

    @Test
    @DisplayName("Test getPassengerById; then success")
    void testGetPassengerById_thenSuccess() {
        // Arrange
        Long passengerId = 1L;
        when(passengerRepository.findByIdAndDeletedIsFalse(any(Long.class)))
                .thenReturn(Optional.of(AppTestUtil.passenger));
        when(passengerMapper.toDto(any(Passenger.class)))
                .thenReturn(AppTestUtil.passengerDto);

        // Act
        passengerServiceImpl.getPassengerById(passengerId);

        // Assert
        verify(passengerRepository).findByIdAndDeletedIsFalse(passengerId);
    }

}
