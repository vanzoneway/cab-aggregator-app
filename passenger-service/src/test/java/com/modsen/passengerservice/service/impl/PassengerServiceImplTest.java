package com.modsen.passengerservice.service.impl;


import com.modsen.passengerservice.dto.ListContainerResponseDto;
import com.modsen.passengerservice.dto.PassengerDto;
import com.modsen.passengerservice.exception.passenger.DuplicatePassengerPhoneOrEmailException;
import com.modsen.passengerservice.exception.passenger.PassengerNotFoundException;
import com.modsen.passengerservice.mapper.ListContainerMapper;
import com.modsen.passengerservice.mapper.PassengerMapper;
import com.modsen.passengerservice.model.Passenger;
import com.modsen.passengerservice.repository.PassengerRepository;

import org.junit.jupiter.api.BeforeEach;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    private Passenger passenger;
    private PassengerDto passengerDto;

    @BeforeEach
    void setup() {
        passenger = new Passenger();
        passenger.setId(1L);
        passenger.setEmail("some.email@gmail.com");
        passenger.setFirstName("John");
        passenger.setLastName("Lock");
        passenger.setAverageRating(5.0);
        passenger.setPhone("+123456789");
        passenger.setDeleted(false);
        passengerDto = new PassengerDto(
                passenger.getId(),
                passenger.getFirstName(),
                passenger.getLastName(),
                passenger.getEmail(),
                passenger.getPhone(),
                passenger.getAverageRating(),
                passenger.getDeleted());
    }

    @Test
    @DisplayName("Test createPassenger(PassengerDto);" +
            "then call checkPassengerExistsByPhoneOrEmail and throw DuplicatePassengerPhoneOrEmailException")
    void testCreatePassenger_thenCallCheckPassengerExistsByPhoneOrEmail() {
        //Arrange
        when(passengerRepository.existsByEmailAndDeletedIsFalse(passenger.getEmail()))
                .thenReturn(true);
        when(messageSource.getMessage(any(), any(Object[].class), any(Locale.class)))
                .thenReturn("An error occurred");

        //Act and Assert
        assertThrows(DuplicatePassengerPhoneOrEmailException.class,
                () -> passengerServiceImpl.createPassenger(passengerDto));
        verify(passengerRepository).existsByEmailAndDeletedIsFalse(passenger.getEmail());
    }

    @Test
    @DisplayName("Test createPassenger(PassengerDto);" +
            "then call checkPassengerRestoreOption and throw DuplicatePassengerPhoneOrEmailException")
    void testCreatePassenger_thenCallCheckPassengerRestoreOption() {
        //Arrange
        when(passengerRepository.existsByEmailAndDeletedIsFalse(passenger.getEmail()))
                .thenReturn(false);
        when(passengerRepository.existsByEmailAndDeletedIsTrue(passenger.getEmail()))
                .thenReturn(true);
        when(messageSource.getMessage(any(), any(Object[].class), any(Locale.class)))
                .thenReturn("An error occurred");

        //Act and Assert
        assertThrows(DuplicatePassengerPhoneOrEmailException.class,
                () -> passengerServiceImpl.createPassenger(passengerDto));
        verify(passengerRepository).existsByEmailAndDeletedIsTrue(passenger.getEmail());
    }

    @Test
    @DisplayName("Test createPassenger(PassengerDto);" +
            "then success")
    void testCreatePassenger_thenSuccess() {
        //Arrange
        when(passengerRepository.existsByEmailAndDeletedIsFalse(passenger.getEmail()))
                .thenReturn(false);
        when(passengerRepository.existsByEmailAndDeletedIsTrue(passenger.getEmail()))
                .thenReturn(false);
        when(passengerMapper.toEntity(any(PassengerDto.class)))
                .thenReturn(passenger);
        when(passengerRepository.save(any(Passenger.class)))
                .thenReturn(passenger);
        when(passengerMapper.toDto(any(Passenger.class))).thenReturn(passengerDto);

        //Act
        PassengerDto passengerResponseDto = passengerServiceImpl.createPassenger(passengerDto);

        //Assert
        assertSame(passengerDto, passengerResponseDto);
        verify(passengerRepository).save(any(Passenger.class));
    }

    @Test
    @DisplayName("Test getPagePassengers; then success")
    void testGetPagePassengers_thenSuccess() {
        //Arrange
        int offset = 1;
        int limit = 10;
        ListContainerResponseDto<PassengerDto> expectedResponse = ListContainerResponseDto.<PassengerDto>builder()
                .withTotalElements(1)
                .withTotalPages(1)
                .withValues(Collections.singletonList(passengerDto))
                .build();
        List<Passenger> passengers = Collections.singletonList(passenger);
        Page<Passenger> passengerPage = new PageImpl<>(passengers);

        when(passengerRepository.findAllByDeletedIsFalse(PageRequest.of(offset, limit)))
                .thenReturn(passengerPage);
        when(passengerMapper.toDto(any(Passenger.class)))
                .thenReturn(passengerDto);
        when(listContainerMapper.toDto(any(Page.class))).thenReturn(expectedResponse);

        //Act
        ListContainerResponseDto<PassengerDto> response = passengerServiceImpl.getPagePassengers(offset, limit);

        //Assert
        assertNotNull(response);
        assertEquals(1, response.totalPages());
        assertEquals(1, response.totalElements());
        assertEquals(passengerDto, response.values().getFirst());
        verify(passengerRepository).findAllByDeletedIsFalse(PageRequest.of(offset, limit));
        verify(passengerMapper).toDto(any(Passenger.class));
    }

    @Test
    @DisplayName("Test safeDeletePassengerById(Long);" +
            "then call getPassengerByIdAndDeletedIsFalse(Long)")
    void testSafeDeletePassengerById_thenCallGetPassengerByIdAndDeletedIsFalse() {
        //Arrange
        Long passengerId = 1L;
        when(passengerRepository.findByIdAndDeletedIsFalse(any(Long.class)))
                .thenReturn(Optional.empty());
        when(messageSource.getMessage(any(), any(Object[].class), any(Locale.class)))
                .thenThrow(PassengerNotFoundException.class);

        //Act and Assert
        assertThrows(PassengerNotFoundException.class, () -> passengerServiceImpl.safeDeletePassengerById(passengerId));
    }

    @Test
    @DisplayName("Test safeDeletePassengerById(Long);" +
            "then success")
    void testSafeDeletePassengerById_thenSuccess() {
        //Arrange
        Long passengerId = 1L;
        when(passengerRepository.findByIdAndDeletedIsFalse(any(Long.class)))
                .thenReturn(Optional.ofNullable(passenger));
        when(passengerRepository.save(any(Passenger.class))).thenReturn(passenger);

        //Act
        passengerServiceImpl.safeDeletePassengerById(passengerId);

        //Assert
        verify(passengerRepository).save(any(Passenger.class));
        verify(passengerRepository).findByIdAndDeletedIsFalse(passengerId);
    }

    @Test
    @DisplayName("Test updatePassengerById; then success")
    void testUpdatePassengerById_thenSuccess() {
        Long passengerId = 1L;
        when(passengerRepository.findByIdAndDeletedIsFalse(any(Long.class)))
                .thenReturn(Optional.ofNullable(passenger));
        doNothing().when(passengerMapper).partialUpdate(any(PassengerDto.class), any(Passenger.class));
        when(passengerRepository.save(any(Passenger.class))).thenReturn(passenger);
        when(passengerMapper.toDto(any(Passenger.class))).thenReturn(passengerDto);

        //Act
        passengerServiceImpl.updatePassengerById(passengerId, passengerDto);

        //Assert
        verify(passengerRepository).save(any(Passenger.class));
        verify(passengerRepository).findByIdAndDeletedIsFalse(passengerId);
    }

    @Test
    @DisplayName("Test getPassengerById; then success")
    void testGetPassengerById_thenSuccess() {
        //Arrange
        Long passengerId = 1L;
        when(passengerRepository.findByIdAndDeletedIsFalse(any(Long.class)))
                .thenReturn(Optional.ofNullable(passenger));
        when(passengerMapper.toDto(any(Passenger.class))).thenReturn(passengerDto);

        //Act
        passengerServiceImpl.getPassengerById(passengerId);

        //Assert
        verify(passengerRepository).findByIdAndDeletedIsFalse(passengerId);

    }
}
