package com.modsen.ridesservice.service;

import com.modsen.ridesservice.dto.ListContainerResponseDto;
import com.modsen.ridesservice.dto.request.RideRequestDto;
import com.modsen.ridesservice.dto.request.RideStatusRequestDto;
import com.modsen.ridesservice.dto.response.RideResponseDto;

public interface RideService {

    RideResponseDto createRide(RideRequestDto rideRequestDto);

    RideResponseDto changeRideStatus(Long rideId, RideStatusRequestDto rideStatusRequestDto);

    RideResponseDto updateRide(Long rideId, RideRequestDto rideRequestDto);

    RideResponseDto getRideById(Long rideId);

    ListContainerResponseDto<RideResponseDto> getPageRides(Integer offset, Integer limit);

    ListContainerResponseDto<RideResponseDto> getPageRidesByDriverId(Long driverId, Integer offset, Integer limit);

    ListContainerResponseDto<RideResponseDto> getPageRidesByPassengerId(Long passengerId, Integer offset, Integer limit);

}
