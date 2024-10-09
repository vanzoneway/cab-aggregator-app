package com.modsen.ratingservice.dto.response;

import java.io.Serializable;

public record RatingResponseDto(
        String comment,
        String userType,
        Integer rating,
        Long rideId) implements Serializable {
}
