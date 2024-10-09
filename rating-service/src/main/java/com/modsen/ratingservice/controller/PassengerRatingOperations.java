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

@Validated
public interface PassengerRatingOperations {

    @Validated(Marker.OnCreate.class)
    RatingResponseDto createPassengerRating(@Valid @RequestBody RatingRequestDto ratingRequestDto);

    @Validated(Marker.OnUpdate.class)
    RatingResponseDto updatePassengerRating(@PathVariable Long id,
                                            @Valid @RequestBody RatingRequestDto ratingRequestDto);

    RatingResponseDto getPassengerRating(@PathVariable Long id);

    ListContainerResponseDto<RatingResponseDto> getPassengerRatingsByRideId(@PathVariable Long rideId,
                                                                            @RequestParam(defaultValue = "0")
                                                                            @Min(0) Integer offset,
                                                                            @RequestParam(defaultValue = "10")
                                                                            @Min(1) @Max(100) Integer limit);

    void deletePassengerRating(@PathVariable Long id);

}
