package com.modsen.ridesservice.dto.request;

import com.modsen.ridesservice.exception.validation.constraints.EnumTypeSubset;
import com.modsen.ridesservice.model.enums.RideStatus;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record RideStatusRequestDto(
        @NotNull(message = "{cost.empty}")
        @EnumTypeSubset(enumClass = RideStatus.class, message = "{cost.positive.zero}")
        String rideStatus) implements Serializable {
}
