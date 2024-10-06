package com.modsen.ridesservice.dto.request;

import com.modsen.ridesservice.exception.validation.constraints.EnumTypeSubset;
import com.modsen.ridesservice.model.enums.RideStatus;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record RideStatusRequestDto(
        @NotNull(message = "{ride.status.empty}")
        @EnumTypeSubset(enumClass = RideStatus.class, message = "{ride.status.wrong.enum.type}")
        String rideStatus) implements Serializable {
}
