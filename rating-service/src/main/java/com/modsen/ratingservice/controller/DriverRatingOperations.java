package com.modsen.ratingservice.controller;

import com.modsen.ratingservice.dto.ListContainerResponseDto;
import com.modsen.ratingservice.dto.Marker;
import com.modsen.ratingservice.dto.request.RatingRequestDto;
import com.modsen.ratingservice.dto.response.RatingResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface DriverRatingOperations {

    @Validated(Marker.OnCreate.class)
    RatingResponseDto createDriverRating(@Valid @RequestBody RatingRequestDto ratingRequestDto);

    RatingResponseDto updateDriverRating(@PathVariable Long id,
                                         @Valid @RequestBody RatingRequestDto ratingRequestDto);

    RatingResponseDto getDriverRating(@PathVariable Long id);

    ListContainerResponseDto<RatingResponseDto> getDriverRatingsByRideId(@PathVariable Long rideId,
                                                                         @RequestParam(defaultValue = "0")
                                                                         @Min(0) Integer offset,
                                                                         @RequestParam(defaultValue = "10")
                                                                         @Min(1) @Max(100) Integer limit);

    void deleteDriverRating(@PathVariable Long id);

}
