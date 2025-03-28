package com.modsen.notificationservice.dto.statistic;

import com.modsen.notificationservice.dto.ride.RideStatisticResponseDto;
import lombok.Builder;

import java.io.Serializable;

@Builder(setterPrefix = "with")
public record GeneralUserStatisticDto(
        UserDto user,
        String userType,
        RideStatisticResponseDto rideStatistic) implements Serializable {
}
