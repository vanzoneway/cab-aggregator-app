package com.modsen.ridesservice.service.impl;

import com.modsen.ridesservice.dto.ListContainerResponseDto;
import com.modsen.ridesservice.dto.request.RideRequestDto;
import com.modsen.ridesservice.dto.request.RideStatusRequestDto;
import com.modsen.ridesservice.dto.response.RideResponseDto;
import com.modsen.ridesservice.exception.ride.RideNotFoundException;
import com.modsen.ridesservice.mapper.ListContainerMapper;
import com.modsen.ridesservice.mapper.RideMapper;
import com.modsen.ridesservice.model.Ride;
import com.modsen.ridesservice.model.enums.RideStatus;
import com.modsen.ridesservice.repository.RideRepository;
import com.modsen.ridesservice.service.component.RideServicePriceGenerator;
import com.modsen.ridesservice.service.component.RideServiceValidation;
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
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RideServiceImplTest {

    @Mock
    private RideRepository rideRepository;

    @Mock
    private RideMapper rideMapper;

    @Mock
    private ListContainerMapper listContainerMapper;

    @Mock
    private MessageSource messageSource;

    @Mock
    private RideServicePriceGenerator priceGenerator;

    @Mock
    private RideServiceValidation rideServiceValidation;

    @InjectMocks
    private RideServiceImpl rideService;


    RideRequestDto rideRequestDto;
    RideResponseDto rideResponseDto;
    RideStatusRequestDto rideStatusRequestDto;
    Ride ride;

    @BeforeEach
    void setup() {
        rideRequestDto = new RideRequestDto(
                1L,
                1L,
                "123 Main St",
                "456 Elm St"
        );

        rideStatusRequestDto = new RideStatusRequestDto(
                RideStatus.CREATED.name()
        );

        rideResponseDto = new RideResponseDto(
                1L,
                1L,
                1L,
                "123 Main St",
                "456 Elm St",
                RideStatus.CREATED.name(),
                LocalDateTime.parse("2023-10-31T14:30:00"),
                new BigDecimal("100.00")
        );

        ride = new Ride();
        ride.setId(1L);
        ride.setDriverId(1L);
        ride.setPassengerId(1L);
        ride.setDepartureAddress("123 Main St");
        ride.setDestinationAddress("456 Elm St");
        ride.setRideStatus(RideStatus.CREATED);
        ride.setOrderDateTime(LocalDateTime.parse("2023-10-31T14:30:00"));
        ride.setCost(new BigDecimal("100.00"));
    }

    @Test
    @DisplayName("Test createRide(RideRequestDto); then success")
    void testCreateRide_thenSuccess() {
        //Arrange
        doNothing().when(rideServiceValidation).checkExistingPassengerOrDriver(any(RideRequestDto.class));
        when(rideMapper.toEntity(any(RideRequestDto.class))).thenReturn(ride);
        when(priceGenerator.generateRandomCost()).thenReturn(new BigDecimal("100.00"));
        when(rideRepository.save(any(Ride.class))).thenReturn(ride);
        when(rideMapper.toDto(any(Ride.class))).thenReturn(rideResponseDto);

        //Act
        RideResponseDto result = rideService.createRide(rideRequestDto);

        //Assert
        assertSame(result, rideResponseDto);
        verify(rideServiceValidation).checkExistingPassengerOrDriver(rideRequestDto);
        verify(rideMapper).toDto(ride);
        verify(priceGenerator).generateRandomCost();
        verify(rideServiceValidation).checkExistingPassengerOrDriver(rideRequestDto);
        verify(rideMapper).toDto(ride);

    }

    @Test
    @DisplayName("Test changeRideStatus(Long, RideStatusRequestDto); then success")
    void testChangeRideStatus_thenSuccess() {
        //Arrange
        doNothing().when(rideServiceValidation)
                .validateChangingRideStatus(any(Ride.class), any(RideStatusRequestDto.class));
        doNothing().when(rideMapper).partialUpdate(any(RideStatusRequestDto.class), any(Ride.class));
        when(rideRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(ride));
        when(rideRepository.save(any(Ride.class))).thenReturn(ride);
        when(rideMapper.toDto(any(Ride.class))).thenReturn(rideResponseDto);

        //Act
        rideService.changeRideStatus(1L, rideStatusRequestDto);

        //Assert
        verify(rideServiceValidation).validateChangingRideStatus(any(Ride.class), any(RideStatusRequestDto.class));
        verify(rideMapper).partialUpdate(any(RideStatusRequestDto.class), any(Ride.class));
        verify(rideRepository).save(any(Ride.class));
    }

    @Test
    @DisplayName("Test updateRide; then success")
    void testUpdateRide_thenSuccess() {
        //Arrange
        doNothing().when(rideMapper).partialUpdate(any(RideRequestDto.class), any(Ride.class));
        when(rideRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(ride));
        when(rideRepository.save(any(Ride.class))).thenReturn(ride);
        when(rideMapper.toDto(any(Ride.class))).thenReturn(rideResponseDto);
        doNothing().when(rideServiceValidation).checkExistingPassengerOrDriver(any(RideRequestDto.class));

        //Act
        rideService.updateRide(1L, rideRequestDto);

        //Assert
        verify(rideMapper).partialUpdate(any(RideRequestDto.class), any(Ride.class));
        verify(rideRepository).save(any(Ride.class));
    }

    @Test
    @DisplayName("Test getRideById(Long); then success and throw RideNotFoundException")
    void testGetRideById_thenCallGetRideAndThrowRideNotFoundException() {
        //Arrange
        when(rideRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        when(messageSource.getMessage(any(), any(Object[].class), any(Locale.class)))
                .thenReturn("An error occurred");

        //Act and Assert
        assertThrows(RideNotFoundException.class, () -> rideService.getRideById(1L));
    }

    @Test
    @DisplayName("Test getRideById(Long); then success")
    void testGetRideById_thenSuccess() {
        //Arrange
        when(rideRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(ride));
        when(rideMapper.toDto(any(Ride.class))).thenReturn(rideResponseDto);

        //Act and Assert
        assertSame(rideResponseDto, rideService.getRideById(1L));
    }

    @Test
    @DisplayName("Test getPageRides(Integer, Integer); then success")
    void testGetPageRides_thenSuccess() {
        //Arrange
        int offset = 1;
        int limit = 10;
        ListContainerResponseDto<RideResponseDto> expectedResponse = ListContainerResponseDto.<RideResponseDto>builder()
                .withTotalElements(1)
                .withTotalPages(1)
                .withValues(Collections.singletonList(rideResponseDto))
                .build();
        List<Ride> rides = Collections.singletonList(ride);
        Page<Ride> ridePage = new PageImpl<>(rides);
        when(rideRepository.findAll(any(Pageable.class))).thenReturn(ridePage);
        arrangeForTestingPageMethods(expectedResponse);

        //Act
        ListContainerResponseDto<RideResponseDto> response = rideService.getPageRides(offset, limit);

        //Assert
        assertSame(expectedResponse, response);
        verify(rideMapper).toDto(ride);
        verify(listContainerMapper).toDto(any(Page.class));
    }

    @Test
    @DisplayName("Test getPageRidesByDriverId(Long, Integer, Integer); then success")
    void testGetPageRidesByDriverId_thenSuccess() {
        //Arrange
        int offset = 1;
        int limit = 10;
        ListContainerResponseDto<RideResponseDto> expectedResponse = ListContainerResponseDto.<RideResponseDto>builder()
                .withTotalElements(1)
                .withTotalPages(1)
                .withValues(Collections.singletonList(rideResponseDto))
                .build();
        List<Ride> rides = Collections.singletonList(ride);
        Page<Ride> ridePage = new PageImpl<>(rides);
        when(rideRepository.findAllByDriverId(any(Long.class), any(Pageable.class))).thenReturn(ridePage);
        arrangeForTestingPageMethods(expectedResponse);

        //Act
        ListContainerResponseDto<RideResponseDto> response = rideService
                .getPageRidesByDriverId(1L, offset, limit);

        //Assert
        assertSame(expectedResponse, response);
        verify(rideMapper).toDto(ride);
        verify(listContainerMapper).toDto(any(Page.class));
    }

    @Test
    @DisplayName("Test getPageRidesByPassengerId(Integer, Integer); then success")
    void testGetPageRidesByPassengerId_thenSuccess() {
        int offset = 1;
        int limit = 10;
        ListContainerResponseDto<RideResponseDto> expectedResponse = ListContainerResponseDto.<RideResponseDto>builder()
                .withTotalElements(1)
                .withTotalPages(1)
                .withValues(Collections.singletonList(rideResponseDto))
                .build();
        List<Ride> rides = Collections.singletonList(ride);
        Page<Ride> ridePage = new PageImpl<>(rides);
        when(rideRepository.findAllByPassengerId(any(Long.class), any(Pageable.class))).thenReturn(ridePage);
        arrangeForTestingPageMethods(expectedResponse);

        //Act
        ListContainerResponseDto<RideResponseDto> response = rideService
                .getPageRidesByPassengerId(1L,offset, limit);

        //Assert
        assertSame(expectedResponse, response);
        verify(rideMapper).toDto(ride);
        verify(listContainerMapper).toDto(any(Page.class));
    }

    private void arrangeForTestingPageMethods(ListContainerResponseDto<RideResponseDto> expectedResponse) {
        when(rideMapper.toDto(any(Ride.class))).thenReturn(rideResponseDto);
        when(listContainerMapper.toDto(any(Page.class))).thenReturn(expectedResponse);
    }

}
