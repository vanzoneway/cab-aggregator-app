package com.modsen.ridesservice.controller.impl;

import com.modsen.ridesservice.controller.RideOperations;
import com.modsen.ridesservice.dto.ListContainerResponseDto;
import com.modsen.ridesservice.dto.request.RideRequestDto;
import com.modsen.ridesservice.dto.request.RideStatusRequestDto;
import com.modsen.ridesservice.dto.response.RideResponseDto;
import com.modsen.ridesservice.service.RideService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RideResponseDto createRide(@Valid @RequestBody RideRequestDto rideRequestDto) {
        return rideService.createRide(rideRequestDto);
    }

    @Override
    @GetMapping
    public ListContainerResponseDto<RideResponseDto> getPageRides(@Min(0) Integer offset,
                                                                  @Min(1) @Max(100) Integer limit) {
        return rideService.getPageRides(offset, limit);
    }

    @Override
    @PatchMapping("/{rideId}/status")
    public RideResponseDto changeRideStatus(
            @PathVariable Long rideId,
            @RequestBody @Valid RideStatusRequestDto rideStatusRequestDto) {
        return rideService.changeStatusRide(rideId, rideStatusRequestDto);
    }

    @Override
    @PutMapping("/{rideId}")
    public RideResponseDto updateRide(@PathVariable Long rideId,
                                      @RequestBody @Valid RideRequestDto rideRequestDto) {
        return rideService.updateRide(rideId, rideRequestDto);
    }

    @Override
    @GetMapping("/rideId")
    public RideResponseDto getRideById(Long rideId) {
        return rideService.getRideById(rideId);
    }

}
