package com.modsen.ridesservice.controller.impl;

import com.modsen.ridesservice.controller.RideOperations;
import com.modsen.ridesservice.dto.ListContainerResponseDto;
import com.modsen.ridesservice.dto.request.RideRequestDto;
import com.modsen.ridesservice.dto.request.RideStatusRequestDto;
import com.modsen.ridesservice.dto.response.RideResponseDto;
import com.modsen.ridesservice.dto.response.RideStatisticResponseDto;
import com.modsen.ridesservice.service.RideService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/rides")
@RequiredArgsConstructor
public class RideController implements RideOperations {

    private final RideService rideService;

    @Override
    @GetMapping
    public ListContainerResponseDto<RideResponseDto> getPageRides(@Min(0) Integer offset,
                                                                  @Min(1) @Max(100) Integer limit) {
        return rideService.getPageRides(offset, limit);
    }

    @Override
    @GetMapping("/{rideId}")
    public RideResponseDto getRideById(@PathVariable Long rideId) {
        return rideService.getRideById(rideId);
    }

    @Override
    @GetMapping("/drivers/{driverId}")
    public ListContainerResponseDto<RideResponseDto> getPageRidesByDriverId(@PathVariable Long driverId,
                                                                            @Min(0) Integer offset,
                                                                            @Min(1) @Max(100) Integer limit) {
        return rideService.getPageRidesByDriverId(driverId, offset, limit);
    }

    @Override
    @GetMapping("/passengers/{passengerId}")
    public ListContainerResponseDto<RideResponseDto> getPageRidesByPassengerId(@PathVariable Long passengerId,
                                                                               @Min(0) Integer offset,
                                                                               @Min(1) @Max(100) Integer limit) {
        return rideService.getPageRidesByPassengerId(passengerId, offset, limit);
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN') or hasRole('PASSENGER')")
    public RideResponseDto createRide(@Valid @RequestBody RideRequestDto rideRequestDto) {
        return rideService.createRide(rideRequestDto);
    }

    @Override
    @PatchMapping("/{rideId}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER') or hasRole('PASSENGER')")
    public RideResponseDto changeRideStatus(
            @PathVariable Long rideId,
            @RequestBody @Valid RideStatusRequestDto rideStatusRequestDto) {
        return rideService.changeRideStatus(rideId, rideStatusRequestDto);
    }

    @Override
    @PutMapping("/{rideId}")
    @PreAuthorize("hasRole('ADMIN')")
    public RideResponseDto updateRide(@PathVariable Long rideId,
                                      @RequestBody @Valid RideRequestDto rideRequestDto) {
        return rideService.updateRide(rideId, rideRequestDto);
    }

    @Override
    @GetMapping("/passengers/{passengerId}/statistics")
    public RideStatisticResponseDto getRideStatisticForPassenger(@PathVariable("passengerId") Long passengerId) {
        return rideService.getRideStatisticForPassenger(passengerId);
    }

    @Override
    @GetMapping("/drivers/{driverId}/statistics")
    public RideStatisticResponseDto getRideStatisticForDriver(@PathVariable("driverId") Long driverId) {
        return rideService.getRideStatisticForDriver(driverId);
    }

}
