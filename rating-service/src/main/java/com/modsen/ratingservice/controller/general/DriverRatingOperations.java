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

@Tag(name = "Driver Ratings", description = """
         This API provides endpoints for managing driver ratings, including creating, updating,\s
         retrieving, deleting ratings, and calculating average ratings. For example, if passenger want to left a review
         to the driver - he must use this controller
        \s""")
@Validated
public interface DriverRatingOperations {

    @Operation(summary = "Create a new driver rating",
            description = """
                    Required fields:\s
                    - **comment**: Comment about the rating (non-empty string)
                    - **rating**: Rating score (integer from 1 to 5)
                    - **rideId**: ID of the ride associated with the rating
                    Example:\s
                    {
                      "comment": "Great driver!",
                      "rating": 5,
                      "refUserId": 1,
                      "rideId": 1001
                    }""")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Driver rating created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided")
    })
    @Validated(Marker.OnCreate.class)
    RatingResponseDto createDriverRating(
            @Parameter(description = "Driver rating details to be created", required = true)
            @RequestBody @Valid RatingRequestDto ratingRequestDto);

    @Operation(summary = "Update an existing driver rating",
            description = """
                    Required fields:\s
                    - **comment**: Updated comment about the rating
                    - **rating**: Updated rating score (integer from 1 to 5)
                    Example:\s
                    {
                      "comment": "Updated comment",
                      "rating": 4
                    }""")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Driver rating updated successfully"),
            @ApiResponse(responseCode = "404", description = "Driver rating not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided")
    })
    @Validated(Marker.OnUpdate.class)
    RatingResponseDto updateDriverRating(
            @Parameter(description = "ID of the rating to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated driver rating details", required = true)
            @RequestBody @Valid RatingRequestDto ratingRequestDto);

    @Operation(summary = "Retrieve a driver rating by ID",
            description = "Fetches the details of a specific driver rating.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Driver rating retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Driver rating not found")
    })
    RatingResponseDto getDriverRating(
            @Parameter(description = "ID of the rating to retrieve", required = true)
            @PathVariable Long id);

    @Operation(summary = "Get driver ratings by ride ID",
            description = "Retrieves a paginated list of driver ratings for a specific ride.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Driver ratings retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    ListContainerResponseDto<RatingResponseDto> getDriverRatingsByRefUserId(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable Long refUserId,
            @Parameter(description = "Pagination offset", example = "0")
            @RequestParam(defaultValue = "0") @Min(0) Integer offset,
            @Parameter(description = "Pagination limit", example = "10")
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer limit);

    @Operation(summary = "Delete a driver rating",
            description = "Deletes the specified driver rating.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Driver rating deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Driver rating not found")
    })
    void deleteDriverRating(
            @Parameter(description = "ID of the rating to delete", required = true)
            @PathVariable Long id);

    @Operation(summary = "Get average driver rating",
            description = "Calculates the average rating for a specific driver.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Average rating retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Driver not found")
    })
    AverageRatingResponseDto averageDriverRating(
            @Parameter(description = "ID of the driver to retrieve average rating", required = true)
            @PathVariable Long refUserId);

}

