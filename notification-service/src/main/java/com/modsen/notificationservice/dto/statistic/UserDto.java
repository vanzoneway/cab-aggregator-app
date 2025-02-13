package com.modsen.notificationservice.dto.statistic;

import java.io.Serializable;

public record UserDto(
        String firstName,
        String lastName,
        String email,
        String phone,
        Double averageRating) implements Serializable {
}
