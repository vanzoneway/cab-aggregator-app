package com.modsen.ridesservice.dto.request;

import com.modsen.ridesservice.dto.Marker;
import com.modsen.ridesservice.exception.validation.constraints.EnumTypeSubset;
import com.modsen.ridesservice.model.enums.RideStatus;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.io.Serializable;
import java.math.BigDecimal;

public record RideRequestDto(
        @NotNull(groups = Marker.OnCreate.class, message = "{driver.id.empty}")
        Long driverId,

        @NotNull(groups = Marker.OnCreate.class, message = "{passenger.id.empty}")
        Long passengerId,

        @NotBlank(groups = Marker.OnCreate.class, message = "{departure.address.empty}")
        String departureAddress,

        @NotBlank(groups = Marker.OnCreate.class, message = "{destination.address.empty}")
        String destinationAddress,

        @NotBlank(groups = Marker.OnCreate.class, message = "{ride.status.empty}")
        @EnumTypeSubset(enumClass = RideStatus.class, message = "{ride.status.wrong.enum.type}")
        String rideStatus,

        @NotNull(groups = Marker.OnCreate.class, message = "{cost.empty}")
        @PositiveOrZero(groups = {Marker.OnCreate.class, Marker.OnUpdate.class}, message = "{cost.positive.zero}")
        @Digits(integer = 5, fraction = 2,
                groups = {Marker.OnCreate.class, Marker.OnUpdate.class}, message = "{cost.digits}")
        BigDecimal cost) implements Serializable {
}