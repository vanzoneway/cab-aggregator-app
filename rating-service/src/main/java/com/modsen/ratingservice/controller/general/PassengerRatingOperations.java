package com.modsen.ratingservice.controller.general;

import com.modsen.ratingservice.dto.ListContainerResponseDto;
import com.modsen.ratingservice.dto.Marker;
import com.modsen.ratingservice.dto.request.RatingRequestDto;
import com.modsen.ratingservice.dto.response.AverageRatingResponseDto;
import com.modsen.ratingservice.dto.response.RatingResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@Validated
public interface PassengerRatingOperations {

    @Validated(Marker.OnCreate.class)
    RatingResponseDto createPassengerRating(
            @Valid @RequestBody RatingRequestDto ratingRequestDto
    );

    @Validated(Marker.OnUpdate.class)
    RatingResponseDto updatePassengerRating(
            @Parameter(description = "ID of the rating to update", required = true)
            @PathVariable Long id,
            @Valid @RequestBody RatingRequestDto ratingRequestDto
    );

    RatingResponseDto getPassengerRating(
            @Parameter(description = "ID of the rating to retrieve", required = true)
            @PathVariable Long id
    );

    ListContainerResponseDto<RatingResponseDto> getPassengerRatingsByRideId(
            @Parameter(description = "ID of the ride", required = true)
            @PathVariable Long rideId,
            @Parameter(description = "Pagination offset", required = false, example = "0")
            @RequestParam(defaultValue = "0") @Min(0) Integer offset,
            @Parameter(description = "Pagination limit", required = false, example = "10")
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer limit
    );

    void deletePassengerRating(
            @Parameter(description = "ID of the rating to delete", required = true)
            @PathVariable Long id
    );

    AverageRatingResponseDto averagePassengerRating(@PathVariable Long refUserId);

}