package com.modsen.ratingservice.controller.general;

import com.modsen.ratingservice.dto.ListContainerResponseDto;
import com.modsen.ratingservice.dto.Marker;
import com.modsen.ratingservice.dto.request.RatingRequestDto;
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

    @Operation(summary = "Create a new rating to ride from the driver",
            description = "Creates a new rating for ride from the driver's side based on the provided rating details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rating created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = """
                    Conflict on riderId field. Exists only one rating for one ride from the driver
                    """)
    })
    @Validated(Marker.OnCreate.class)
    RatingResponseDto createDriverRating(
            @Valid @RequestBody RatingRequestDto ratingRequestDto
    );

    @Operation(summary = "Update an existing rating for ride from the driver",
            description = "Updates a new rating for ride from the driver's side based on the provided rating details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rating updated successfully"),
            @ApiResponse(responseCode = "404", description = "Rating not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @Validated(Marker.OnUpdate.class)
    RatingResponseDto updateDriverRating(
            @Parameter(description = "ID of the rating to update", required = true)
            @PathVariable Long id,
            @Valid @RequestBody RatingRequestDto ratingRequestDto
    );

    @Operation(summary = "Get a rating for ride from the driver",
            description = "Retrieves the rating for ride from the driver by rating's ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rating retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Rating not found")
    })
    RatingResponseDto getDriverRating(
            @Parameter(description = "ID of the rating to retrieve", required = true)
            @PathVariable Long id
    );

    @Operation(summary = "Get ratings for ride from the driver by ride's ID",
            description = "Retrieves a paginated list of ratings from the driver associated with a specific ride's ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ratings retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Ride not found")
    })
    ListContainerResponseDto<RatingResponseDto> getDriverRatingsByRideId(
            @Parameter(description = "ID of the ride", required = true)
            @PathVariable Long rideId,
            @Parameter(description = "Pagination offset", required = false, example = "0")
            @RequestParam(defaultValue = "0") @Min(0) Integer offset,
            @Parameter(description = "Pagination limit", required = false, example = "10")
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer limit
    );

    @Operation(summary = "Delete a rating for ride from the driver",
            description = "Deletes the rating for ride from the driver by rating's ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Rating deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Rating not found")

    })
    void deleteDriverRating(
            @Parameter(description = "ID of the rating to delete", required = true)
            @PathVariable Long id
    );
}
