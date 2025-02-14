package com.modsen.ridesservice.dto.response;

import lombok.Builder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Builder(setterPrefix = "with")
public record RideStatisticResponseDto(
        Long userRefId,
        BigDecimal averageCost,
        List<RideResponseDto> rides
) implements Serializable {
}
