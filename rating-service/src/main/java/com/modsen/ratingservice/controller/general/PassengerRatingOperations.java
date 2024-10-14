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


@Tag(name = "Passenger Ratings", description = """
         This API provides endpoints for managing passenger ratings, including creating, updating,\s
         retrieving, deleting ratings, and calculating average ratings. For example, if a driver wants to leave a review
         for a passenger, they must use this controller.
        \s""")
@Validated
public interface PassengerRatingOperations {

    @Operation(summary = "Create a new passenger rating",
            description = """
                    Required fields:\s
                    - **comment**: Comment about the rating (non-empty string)
                    - **rating**: Rating score (integer from 1 to 5)
                    - **refUserId**: ID of the user who is giving the rating
                    - **rideId**: ID of the ride associated with the rating
                    Example:\s
                    {
                      "comment": "Great passenger!",
                      "rating": 5,
                      "refUserId": 1,
                      "rideId": 1001
                    }""")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Passenger rating created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided")
    })
    @Validated(Marker.OnCreate.class)
    RatingResponseDto createPassengerRating(
            @Parameter(description = "Passenger rating details to be created", required = true)
            @RequestBody @Valid RatingRequestDto ratingRequestDto);

    @Operation(summary = "Update an existing passenger rating",
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
            @ApiResponse(responseCode = "200", description = "Passenger rating updated successfully"),
            @ApiResponse(responseCode = "404", description = "Passenger rating not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided")
    })
    @Validated(Marker.OnUpdate.class)
    RatingResponseDto updatePassengerRating(
            @Parameter(description = "ID of the rating to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated passenger rating details", required = true)
            @RequestBody @Valid RatingRequestDto ratingRequestDto);

    @Operation(summary = "Retrieve a passenger rating by ID",
            description = "Fetches the details of a specific passenger rating.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Passenger rating retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Passenger rating not found")
    })
    RatingResponseDto getPassengerRating(
            @Parameter(description = "ID of the rating to retrieve", required = true)
            @PathVariable Long id);

    @Operation(summary = "Get passenger ratings by ride ID",
            description = "Retrieves a paginated list of passenger ratings for a specific ride.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Passenger ratings retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    ListContainerResponseDto<RatingResponseDto> getPassengerRatingsByRefUserId(
            @Parameter(description = "ID of the refUserId", required = true)
            @PathVariable Long refUserId,
            @Parameter(description = "Pagination offset", example = "0")
            @RequestParam(defaultValue = "0") @Min(0) Integer offset,
            @Parameter(description = "Pagination limit", example = "10")
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer limit);

    @Operation(summary = "Delete a passenger rating",
            description = "Deletes the specified passenger rating.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Passenger rating deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Passenger rating not found")
    })
    void deletePassengerRating(
            @Parameter(description = "ID of the rating to delete", required = true)
            @PathVariable Long id);

    @Operation(summary = "Get average passenger rating",
            description = "Calculates the average rating for a specific passenger.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Average rating retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Passenger not found")
    })
    AverageRatingResponseDto averagePassengerRating(
            @Parameter(description = "ID of the passenger to retrieve average rating", required = true)
            @PathVariable Long refUserId);

}