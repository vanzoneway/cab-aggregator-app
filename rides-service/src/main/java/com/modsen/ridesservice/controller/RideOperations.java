package com.modsen.ridesservice.controller;

import com.modsen.ridesservice.dto.ListContainerResponseDto;
import com.modsen.ridesservice.dto.Marker;
import com.modsen.ridesservice.dto.request.RideRequestDto;
import com.modsen.ridesservice.dto.request.RideStatusRequestDto;
import com.modsen.ridesservice.dto.response.RideResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Validated
public interface RideOperations {

    @Validated(Marker.OnCreate.class)
    RideResponseDto createRide(@RequestBody @Valid RideRequestDto rideRequestDto);

    ListContainerResponseDto<RideResponseDto> getPageRides(
            @RequestParam(defaultValue = "0") @Min(0) Integer offset,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer limit);

    RideResponseDto changeRideStatus(@PathVariable Long rideId,
                                     @RequestBody @Valid RideStatusRequestDto rideStatusRequestDto);

    @Validated(Marker.OnUpdate.class)
    RideResponseDto updateRide(@PathVariable Long rideId,
                               @RequestBody @Valid RideRequestDto rideRequestDto);

    RideResponseDto getRideById(@PathVariable Long rideId);

}
