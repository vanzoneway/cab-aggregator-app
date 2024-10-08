package com.modsen.ridesservice.dto.request;

import com.modsen.ridesservice.dto.Marker;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.io.Serializable;
import java.math.BigDecimal;

public record RideRequestDto(
        Long driverId,

        @NotNull(groups = Marker.OnCreate.class, message = "{passenger.id.empty}")
        Long passengerId,

        @NotBlank(groups = Marker.OnCreate.class, message = "{departure.address.empty}")
        String departureAddress,

        @NotBlank(groups = Marker.OnCreate.class, message = "{destination.address.empty}")
        String destinationAddress,

        @NotNull(groups = Marker.OnCreate.class, message = "{cost.empty}")
        @PositiveOrZero(groups = {Marker.OnCreate.class, Marker.OnUpdate.class}, message = "{cost.positive.zero}")
        @Digits(integer = 5, fraction = 2,
                groups = {Marker.OnCreate.class, Marker.OnUpdate.class}, message = "{cost.digits}")
        BigDecimal cost) implements Serializable {
}