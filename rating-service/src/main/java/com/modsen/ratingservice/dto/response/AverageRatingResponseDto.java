package com.modsen.ratingservice.dto.response;

public record AverageRatingResponseDto(
        Long refUserId,
        Double averageRating) {
}
