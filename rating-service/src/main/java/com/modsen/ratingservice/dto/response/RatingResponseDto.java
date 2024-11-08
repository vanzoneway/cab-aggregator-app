package com.modsen.ratingservice.dto.response;

import java.io.Serializable;

public record RatingResponseDto(
        Long id,
        String comment,
        String userType,
        Long refUserId,
        Integer rating,
        Long rideId) implements Serializable {
}
