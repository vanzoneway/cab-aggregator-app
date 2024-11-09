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
    void createPassenger_CallCheckPassengerExistsByPhoneOrEmail_SuchPassengerAlreadyExists() {
        // Arrange
        when(passengerRepository.existsByEmailAndDeletedIsFalse(AppTestUtil.PASSENGER.getEmail()))
                .thenReturn(true);
        when(messageSource.getMessage(any(), any(Object[].class), any(Locale.class)))
                .thenReturn("");

        // Act and Assert
        assertThatThrownBy(() -> passengerServiceImpl.createPassenger(AppTestUtil.PASSENGER_DTO))
                .isInstanceOf(DuplicatePassengerPhoneOrEmailException.class);

        verify(passengerRepository).existsByEmailAndDeletedIsFalse(AppTestUtil.PASSENGER.getEmail());
    }

    @Test
    void createPassenger_CallCheckPassengerRestoreOption_SuchPassengerWereDeleted() {
        // Arrange
        when(passengerRepository.existsByEmailAndDeletedIsFalse(AppTestUtil.PASSENGER.getEmail()))
                .thenReturn(false);
        when(passengerRepository.existsByEmailAndDeletedIsTrue(AppTestUtil.PASSENGER.getEmail()))
                .thenReturn(true);
        when(messageSource.getMessage(any(), any(Object[].class), any(Locale.class)))
                .thenReturn("");

        // Act and Assert
        assertThatThrownBy(() -> passengerServiceImpl.createPassenger(AppTestUtil.PASSENGER_DTO))
                .isInstanceOf(DuplicatePassengerPhoneOrEmailException.class);

        verify(passengerRepository).existsByEmailAndDeletedIsTrue(AppTestUtil.PASSENGER.getEmail());
    }

    @Test
    void createPassenger_ReturnsCreatedPassengerDto_ValidInputArguments() {
        // Arrange
        when(passengerRepository.existsByEmailAndDeletedIsFalse(AppTestUtil.PASSENGER.getEmail()))
                .thenReturn(false);
        when(passengerRepository.existsByEmailAndDeletedIsTrue(AppTestUtil.PASSENGER.getEmail()))
                .thenReturn(false);
        when(passengerMapper.toEntity(any(PassengerDto.class)))
                .thenReturn(AppTestUtil.PASSENGER);
        when(passengerRepository.save(any(Passenger.class)))
                .thenReturn(AppTestUtil.PASSENGER);
        when(passengerMapper.toDto(any(Passenger.class)))
                .thenReturn(AppTestUtil.PASSENGER_DTO);

        // Act
        PassengerDto actual = passengerServiceImpl.createPassenger(AppTestUtil.PASSENGER_DTO);

        // Assert
        assertThat(actual)
                .isNotNull()
                .isEqualTo(AppTestUtil.PASSENGER_DTO);

        verify(passengerRepository).save(any(Passenger.class));
    }

    @Test
    void getPagePassengers_ReturnsPagePassengerDto_ValidInputArguments() {
        // Arrange
        int offset = 1;
        int limit = 10;
        ListContainerResponseDto<PassengerDto> expectedResponse = ListContainerResponseDto.<PassengerDto>builder()
                .withTotalElements(1)
                .withTotalPages(1)
                .withValues(Collections.singletonList(AppTestUtil.PASSENGER_DTO))
                .build();
        List<Passenger> passengers = Collections.singletonList(AppTestUtil.PASSENGER);
        Page<Passenger> passengerPage = new PageImpl<>(passengers);

        when(passengerRepository.findAllByDeletedIsFalse(PageRequest.of(offset, limit)))
                .thenReturn(passengerPage);
        when(passengerMapper.toDto(any(Passenger.class)))
                .thenReturn(AppTestUtil.PASSENGER_DTO);
        when(listContainerMapper.toDto(any(Page.class)))
                .thenReturn(expectedResponse);

        // Act
        ListContainerResponseDto<PassengerDto> actual = passengerServiceImpl.getPagePassengers(offset, limit);

        // Assert
        assertThat(actual)
                .isNotNull()
                .extracting(ListContainerResponseDto::totalPages, ListContainerResponseDto::totalElements,
                        r -> r.values().getFirst())
                .containsExactly(1, 1L, AppTestUtil.PASSENGER_DTO);

        verify(passengerRepository).findAllByDeletedIsFalse(PageRequest.of(offset, limit));
        verify(passengerMapper).toDto(any(Passenger.class));
    }

    @Test
    void safeDeletePassengerById_CallGetPassengerByIdAndDeletedIsFalse_PassengerDoesntExists() {
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
    void safeDeletePassengerById_PassengerSuccessfullyDeleted_ValidInputArguments() {
        // Arrange
        Long passengerId = 1L;
        when(passengerRepository.findByIdAndDeletedIsFalse(any(Long.class)))
                .thenReturn(Optional.of(AppTestUtil.PASSENGER));
        when(passengerRepository.save(any(Passenger.class)))
                .thenReturn(AppTestUtil.PASSENGER);

        // Act
        passengerServiceImpl.safeDeletePassengerById(passengerId);

        // Assert
        verify(passengerRepository).save(any(Passenger.class));
        verify(passengerRepository).findByIdAndDeletedIsFalse(passengerId);
    }

    @Test
    void updatePassengerById_ReturnsUpdatedPassengerDto_ValidInputArguments() {
        Long passengerId = 1L;
        when(passengerRepository.findByIdAndDeletedIsFalse(any(Long.class)))
                .thenReturn(Optional.of(AppTestUtil.PASSENGER));
        doNothing()
                .when(passengerMapper).partialUpdate(any(PassengerDto.class), any(Passenger.class));
        when(passengerRepository.save(any(Passenger.class)))
                .thenReturn(AppTestUtil.PASSENGER);
        when(passengerMapper.toDto(any(Passenger.class)))
                .thenReturn(AppTestUtil.PASSENGER_DTO);

        // Act
        passengerServiceImpl.updatePassengerById(passengerId, AppTestUtil.PASSENGER_DTO);

        // Assert
        verify(passengerRepository).save(any(Passenger.class));
        verify(passengerRepository).findByIdAndDeletedIsFalse(passengerId);
    }

    @Test
    void getPassengerById_ReturnsPassengerDto_ValidInputArguments() {
        // Arrange
        Long passengerId = 1L;
        when(passengerRepository.findByIdAndDeletedIsFalse(any(Long.class)))
                .thenReturn(Optional.of(AppTestUtil.PASSENGER));
        when(passengerMapper.toDto(any(Passenger.class)))
                .thenReturn(AppTestUtil.PASSENGER_DTO);

        // Act
        passengerServiceImpl.getPassengerById(passengerId);

        // Assert
        verify(passengerRepository).findByIdAndDeletedIsFalse(passengerId);
    }

}
