package com.modsen.ratingservice.service.general;

import com.modsen.ratingservice.dto.ListContainerResponseDto;
import com.modsen.ratingservice.dto.request.RatingRequestDto;
import com.modsen.ratingservice.dto.response.AverageRatingResponseDto;
import com.modsen.ratingservice.dto.response.RatingResponseDto;
import com.modsen.ratingservice.model.general.Rating;

public interface CommonRatingService<T extends Rating> {

    RatingResponseDto createRating(RatingRequestDto ratingRequestDto);

    RatingResponseDto updateRatingById(Long id, RatingRequestDto ratingRequestDto);

    RatingResponseDto getRating(Long id);

    ListContainerResponseDto<RatingResponseDto> getRatingsByRideId(Long rideId, Integer offset, Integer limit);

    void safeDeleteRating(Long id);

    AverageRatingResponseDto getAverageRating(Long refUserId);

}
