package com.modsen.ridesservice.service.impl;

import com.modsen.ridesservice.AppTestUtil;
import com.modsen.ridesservice.dto.ListContainerResponseDto;
import com.modsen.ridesservice.dto.request.RideRequestDto;
import com.modsen.ridesservice.dto.request.RideStatusRequestDto;
import com.modsen.ridesservice.dto.response.RideResponseDto;
import com.modsen.ridesservice.exception.ride.RideNotFoundException;
import com.modsen.ridesservice.mapper.ListContainerMapper;
import com.modsen.ridesservice.mapper.RideMapper;
import com.modsen.ridesservice.model.Ride;
import com.modsen.ridesservice.repository.RideRepository;
import com.modsen.ridesservice.service.component.RideServicePriceGenerator;
import com.modsen.ridesservice.service.component.RideServiceValidation;
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
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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


    @Test
    @DisplayName("Test createRide(RideRequestDto); then success")
    void testCreateRide_thenSuccess() {
        // Arrange
        doNothing().when(rideServiceValidation).checkExistingPassengerOrDriver(any(RideRequestDto.class));
        when(rideMapper.toEntity(any(RideRequestDto.class)))
                .thenReturn(AppTestUtil.ride);
        when(priceGenerator.generateRandomCost())
                .thenReturn(new BigDecimal("100.00"));
        when(rideRepository.save(any(Ride.class)))
                .thenReturn(AppTestUtil.ride);
        when(rideMapper.toDto(any(Ride.class)))
                .thenReturn(AppTestUtil.rideResponseInServiceDto);

        // Act
        RideResponseDto actual = rideService.createRide(AppTestUtil.rideRequestInServiceDto);

        // Assert
        assertThat(actual).isSameAs(AppTestUtil.rideResponseInServiceDto);
        verify(rideServiceValidation).checkExistingPassengerOrDriver(AppTestUtil.rideRequestInServiceDto);
        verify(rideMapper).toDto(AppTestUtil.ride);
        verify(priceGenerator).generateRandomCost();
    }

    @Test
    @DisplayName("Test changeRideStatus(Long, RideStatusRequestDto); then success")
    void testChangeRideStatus_thenSuccess() {
        // Arrange
        doNothing().when(rideServiceValidation)
                .validateChangingRideStatus(any(Ride.class), any(RideStatusRequestDto.class));
        doNothing().when(rideMapper).partialUpdate(any(RideStatusRequestDto.class), any(Ride.class));
        when(rideRepository.findById(anyLong()))
                .thenReturn(Optional.of(AppTestUtil.ride));
        when(rideRepository.save(any(Ride.class)))
                .thenReturn(AppTestUtil.ride);
        when(rideMapper.toDto(any(Ride.class)))
                .thenReturn(AppTestUtil.rideResponseInServiceDto);

        // Act
        rideService.changeRideStatus(1L, AppTestUtil.rideStatusRequestDto);

        // Assert
        verify(rideServiceValidation).validateChangingRideStatus(any(Ride.class), any(RideStatusRequestDto.class));
        verify(rideMapper).partialUpdate(any(RideStatusRequestDto.class), any(Ride.class));
        verify(rideRepository).save(any(Ride.class));
    }

    @Test
    @DisplayName("Test updateRide; then success")
    void testUpdateRide_thenSuccess() {
        // Arrange
        doNothing().when(rideMapper).partialUpdate(any(RideRequestDto.class), any(Ride.class));
        when(rideRepository.findById(anyLong()))
                .thenReturn(Optional.of(AppTestUtil.ride));
        when(rideRepository.save(any(Ride.class)))
                .thenReturn(AppTestUtil.ride);
        when(rideMapper.toDto(any(Ride.class)))
                .thenReturn(AppTestUtil.rideResponseInServiceDto);
        doNothing().when(rideServiceValidation).checkExistingPassengerOrDriver(any(RideRequestDto.class));

        // Act
        rideService.updateRide(1L, AppTestUtil.rideRequestInServiceDto);

        // Assert
        verify(rideMapper).partialUpdate(any(RideRequestDto.class), any(Ride.class));
        verify(rideRepository).save(any(Ride.class));
    }

    @Test
    @DisplayName("Test getRideById(Long); then success and throw RideNotFoundException")
    void testGetRideById_thenCallGetRideAndThrowRideNotFoundException() {
        // Arrange
        when(rideRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        when(messageSource.getMessage(any(), any(Object[].class), any(Locale.class)))
                .thenReturn("");

        // Act and Assert
        assertThatThrownBy(() -> rideService.getRideById(1L))
                .isInstanceOf(RideNotFoundException.class);
    }

    @Test
    @DisplayName("Test getRideById(Long); then success")
    void testGetRideById_thenSuccess() {
        // Arrange
        when(rideRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(AppTestUtil.ride));
        when(rideMapper.toDto(any(Ride.class)))
                .thenReturn(AppTestUtil.rideResponseInServiceDto);

        // Act
        RideResponseDto actual = rideService.getRideById(1L);

        // Assert
        assertThat(actual).isSameAs(AppTestUtil.rideResponseInServiceDto);
    }

    @Test
    @DisplayName("Test getPageRides(Integer, Integer); then success")
    void testGetPageRides_thenSuccess() {
        // Arrange
        int offset = 1;
        int limit = 10;
        ListContainerResponseDto<RideResponseDto> expectedResponse = ListContainerResponseDto.<RideResponseDto>builder()
                .withTotalElements(1)
                .withTotalPages(1)
                .withValues(Collections.singletonList(AppTestUtil.rideResponseInServiceDto))
                .build();
        List<Ride> rides = Collections.singletonList(AppTestUtil.ride);
        Page<Ride> ridePage = new PageImpl<>(rides);
        when(rideRepository.findAll(any(Pageable.class)))
                .thenReturn(ridePage);
        arrangeForTestingPageMethods(expectedResponse);

        // Act
        ListContainerResponseDto<RideResponseDto> actual = rideService.getPageRides(offset, limit);

        // Assert
        assertThat(actual).isSameAs(expectedResponse);
        verify(rideMapper).toDto(AppTestUtil.ride);
        verify(listContainerMapper).toDto(any(Page.class));
    }

    @Test
    @DisplayName("Test getPageRidesByDriverId(Long, Integer, Integer); then success")
    void testGetPageRidesByDriverId_thenSuccess() {
        // Arrange
        int offset = 1;
        int limit = 10;
        ListContainerResponseDto<RideResponseDto> expectedResponse = ListContainerResponseDto.<RideResponseDto>builder()
                .withTotalElements(1)
                .withTotalPages(1)
                .withValues(Collections.singletonList(AppTestUtil.rideResponseInServiceDto))
                .build();
        List<Ride> rides = Collections.singletonList(AppTestUtil.ride);
        Page<Ride> ridePage = new PageImpl<>(rides);
        when(rideRepository.findAllByDriverId(anyLong(), any(Pageable.class)))
                .thenReturn(ridePage);
        arrangeForTestingPageMethods(expectedResponse);

        // Act
        ListContainerResponseDto<RideResponseDto> actual = rideService.getPageRidesByDriverId(1L, offset, limit);

        // Assert
        assertThat(actual).isSameAs(expectedResponse);
        verify(rideMapper).toDto(AppTestUtil.ride);
        verify(listContainerMapper).toDto(any(Page.class));
    }

    @Test
    @DisplayName("Test getPageRidesByPassengerId(Integer, Integer); then success")
    void testGetPageRidesByPassengerId_thenSuccess() {
        // Arrange
        int offset = 1;
        int limit = 10;
        ListContainerResponseDto<RideResponseDto> expectedResponse = ListContainerResponseDto.<RideResponseDto>builder()
                .withTotalElements(1)
                .withTotalPages(1)
                .withValues(Collections.singletonList(AppTestUtil.rideResponseInServiceDto))
                .build();
        List<Ride> rides = Collections.singletonList(AppTestUtil.ride);
        Page<Ride> ridePage = new PageImpl<>(rides);
        when(rideRepository.findAllByPassengerId(anyLong(), any(Pageable.class)))
                .thenReturn(ridePage);
        arrangeForTestingPageMethods(expectedResponse);

        // Act
        ListContainerResponseDto<RideResponseDto> actual = rideService
                .getPageRidesByPassengerId(1L, offset, limit);

        // Assert
        assertThat(actual).isSameAs(expectedResponse);
        verify(rideMapper).toDto(AppTestUtil.ride);
        verify(listContainerMapper).toDto(any(Page.class));
    }

    private void arrangeForTestingPageMethods(ListContainerResponseDto<RideResponseDto> expectedResponse) {
        when(rideMapper.toDto(any(Ride.class)))
                .thenReturn(AppTestUtil.rideResponseInServiceDto);
        when(listContainerMapper.toDto(any(Page.class)))
                .thenReturn(expectedResponse);
    }
}

