package com.modsen.notificationservice.dto.ride;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RideResponseDto(
        Long id,
        Long driverId,
        Long passengerId,
        String departureAddress,
        String destinationAddress,
        String rideStatus,
        LocalDateTime orderDateTime,
        BigDecimal cost) implements Serializable {
}
