package com.modsen.ridesservice.dto.request;

import com.modsen.ridesservice.dto.Marker;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record RideRequestDto(
        Long driverId,

        @NotNull(groups = Marker.OnCreate.class, message = "{passenger.id.empty}")
        Long passengerId,

        @NotBlank(groups = Marker.OnCreate.class, message = "{departure.address.empty}")
        String departureAddress,

        @NotBlank(groups = Marker.OnCreate.class, message = "{destination.address.empty}")
        String destinationAddress) implements Serializable {
}