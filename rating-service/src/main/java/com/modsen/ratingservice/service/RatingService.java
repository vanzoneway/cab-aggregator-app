package com.modsen.ratingservice.service;

import com.modsen.ratingservice.dto.ListContainerResponseDto;
import com.modsen.ratingservice.dto.request.RatingRequestDto;
import com.modsen.ratingservice.dto.response.RatingResponseDto;

public interface RatingService {

    RatingResponseDto createRating(RatingRequestDto ratingRequestDto);

    RatingResponseDto updateRatingById(Long id, RatingRequestDto ratingRequestDto);

    RatingResponseDto getRating(Long id);

    ListContainerResponseDto<RatingResponseDto> getRatingsByRideId(Long rideId, Integer offset, Integer limit);

    void safeDeleteRating(Long id);

}
