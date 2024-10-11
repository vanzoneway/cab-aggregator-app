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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Driver's rating controller", description = """
        This controller is designed for various operations related to creating ratings for a specific ride FROM
        THE DRIVER'S SIDE, meaning the driver leaves a comment and a rating for the ride they conducted. In other words,
        the driver is leaving a comment for a specific passenger, as it refers to the same ride.
        Note: each driver and passenger can leave only one comment for a single ride.
        """)
@Validated
public interface DriverRatingOperations {

    @Validated(Marker.OnCreate.class)
    RatingResponseDto createDriverRating(
            @Valid @RequestBody RatingRequestDto ratingRequestDto
    );

    @Validated(Marker.OnUpdate.class)
    RatingResponseDto updateDriverRating(
            @Parameter(description = "ID of the rating to update", required = true)
            @PathVariable Long id,
            @Valid @RequestBody RatingRequestDto ratingRequestDto
    );

    RatingResponseDto getDriverRating(
            @Parameter(description = "ID of the rating to retrieve", required = true)
            @PathVariable Long id
    );

    ListContainerResponseDto<RatingResponseDto> getDriverRatingsByRideId(
            @Parameter(description = "ID of the ride", required = true)
            @PathVariable Long rideId,
            @Parameter(description = "Pagination offset", required = false, example = "0")
            @RequestParam(defaultValue = "0") @Min(0) Integer offset,
            @Parameter(description = "Pagination limit", required = false, example = "10")
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer limit
    );

    void deleteDriverRating(
            @Parameter(description = "ID of the rating to delete", required = true)
            @PathVariable Long id
    );

    AverageRatingResponseDto averageDriverRating(@PathVariable Long refUserId);

}
