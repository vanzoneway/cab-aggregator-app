package com.modsen.ratingservice.dto.response;

import java.io.Serializable;

public record RatingResponseDto(
        Long id,
        String comment,
        String userType,
        Integer refUserId,
        Integer rating,
        Long rideId) implements Serializable {
}
