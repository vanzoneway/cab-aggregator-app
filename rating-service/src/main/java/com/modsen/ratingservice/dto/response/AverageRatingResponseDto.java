package com.modsen.ratingservice.dto.response;

import java.io.Serializable;

public record AverageRatingResponseDto(
        Long refUserId,
        Double averageRating) implements Serializable {
}
