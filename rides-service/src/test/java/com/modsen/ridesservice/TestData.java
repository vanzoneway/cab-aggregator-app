package com.modsen.ridesservice;

import com.modsen.ridesservice.dto.ListContainerResponseDto;
import com.modsen.ridesservice.dto.request.RideRequestDto;
import com.modsen.ridesservice.dto.request.RideStatusRequestDto;
import com.modsen.ridesservice.dto.response.RideResponseDto;
import com.modsen.ridesservice.model.Ride;
import com.modsen.ridesservice.model.enums.RideStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestData {

    public static final String RIDE_PAGE_GET_POST_ENDPOINT = "/api/v1/rides";
    public static final String RIDE_GET_ENDPOINT = "/api/v1/rides/1";
    public static final String RIDE_BY_DRIVER_ID_GET_ENDPOINT = "/api/v1/rides/drivers/{driverId}";
    public static final String RIDE_BY_PASSENGER_ID_GET_ENDPOINT = "/api/v1/rides/passengers/{passengerId}";
    public static final String RIDE_UPDATE_ENDPOINT = "/api/v1/rides/{rideId}";
    public static final String RIDE_CHANGE_RIDE_STATUS_ENDPOINT = "/api/v1/rides/{rideId}/status";

    public static final RideRequestDto RIDE_REQUEST_DTO = new RideRequestDto(
            1L,
            1L,
            "Vilnius",
            "Riga");

    public static final RideResponseDto RIDE_RESPONSE_DTO = new RideResponseDto(
            1L,
            1L,
            1L,
            "Vilnius",
            "Riga",
            "CREATED",
            LocalDateTime.parse("2023-10-31T14:30:00"),
            new BigDecimal(231));

    public static final ListContainerResponseDto<RideResponseDto> RIDE_PAGE_RESPONSE_DTO = new ListContainerResponseDto<>(
            1, 1, 1, 1L,
            "Sort", new ArrayList<>());


    public static final RideRequestDto RIDE_REQUEST_IN_SERVICE_DTO = new RideRequestDto(
            1L,
            1L,
            "123 Main St",
            "456 Elm St"
    );

    public static final RideResponseDto  RIDE_RESPONSE_IN_SERVICE_DTO = new RideResponseDto(
            1L,
            1L,
            1L,
            "123 Main St",
            "456 Elm St",
            RideStatus.CREATED.name(),
            LocalDateTime.parse("2023-10-31T14:30:00"),
            new BigDecimal("100.00")
    );

    public static final RideStatusRequestDto RIDE_STATUS_REQUEST_DTO = new RideStatusRequestDto(
            RideStatus.CREATED.name()
    );

    public static final Ride RIDE;

    static {

        RIDE = new Ride();
        RIDE.setId(1L);
        RIDE.setDriverId(1L);
        RIDE.setPassengerId(1L);
        RIDE.setDepartureAddress("123 Main St");
        RIDE.setDestinationAddress("456 Elm St");
        RIDE.setRideStatus(RideStatus.CREATED);
        RIDE.setOrderDateTime(LocalDateTime.parse("2023-10-31T14:30:00"));
        RIDE.setCost(new BigDecimal("100.00"));

    }

}
