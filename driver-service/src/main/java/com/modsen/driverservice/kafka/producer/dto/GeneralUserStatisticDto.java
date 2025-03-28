package com.modsen.driverservice.kafka.producer.dto;

import com.modsen.driverservice.client.ride.dto.RideStatisticResponseDto;
import lombok.Builder;

import java.io.Serializable;

@Builder(setterPrefix = "with")
public record GeneralUserStatisticDto(
    UserDto user,
    String userType,
    RideStatisticResponseDto rideStatistic
) implements Serializable {
}
