package com.modsen.notificationservice.dto.ride;

import lombok.Builder;

import java.io.Serializable;
import java.util.List;

@Builder(setterPrefix = "with")
public record RideStatisticResponseDto(
        Long refUserId,
        Double averageCost,
        List<RideResponseDto> rides) implements Serializable {
}
