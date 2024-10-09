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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Passenger Rating Controller", description = """
        This controller is designed for various operations related to creating ratings for a specific ride 
        FROM THE PASSENGER'S SIDE, meaning the passenger leaves a comment and a rating for the ride they took. 
        In other words, the passenger is leaving a comment for a specific driver, as it refers to the same ride.
        Note: each driver and passenger can leave only one comment for a single ride.
        """)
@Validated
public interface PassengerRatingOperations {

    @Operation(summary = "Create a new rating for ride from the passenger",
            description = "Creates a new rating for ride from the passenger's side based on the provided " +
                    "rating details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rating created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = """
                    Conflict on rideId field. Exists only one rating for one ride from the passenger
                    """)
    })
    @Validated(Marker.OnCreate.class)
    @PostMapping("/passenger-ratings")
    RatingResponseDto createPassengerRating(
            @Valid @RequestBody RatingRequestDto ratingRequestDto
    );

    @Operation(summary = "Update an existing rating for ride from the passenger",
            description = "Updates an existing rating for ride from the passenger's side based on the provided " +
                    "rating details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rating updated successfully"),
            @ApiResponse(responseCode = "404", description = "Rating not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping("/passenger-ratings/{id}")
    @Validated(Marker.OnUpdate.class)
    RatingResponseDto updatePassengerRating(
            @Parameter(description = "ID of the rating to update", required = true)
            @PathVariable Long id,
            @Valid @RequestBody RatingRequestDto ratingRequestDto
    );

    @Operation(summary = "Get a rating for ride from the passenger",
            description = "Retrieves the rating for ride from the passenger by rating's ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rating retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Rating not found")
    })
    @GetMapping("/passenger-ratings/{id}")
    RatingResponseDto getPassengerRating(
            @Parameter(description = "ID of the rating to retrieve", required = true)
            @PathVariable Long id
    );

    @Operation(summary = "Get ratings for ride from the passenger by ride's ID",
            description = "Retrieves a paginated list of ratings from the passenger associated with a specific " +
                    "ride's ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ratings retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Ride not found")
    })
    @GetMapping("/passenger-ratings/ride/{rideId}")
    ListContainerResponseDto<RatingResponseDto> getPassengerRatingsByRideId(
            @Parameter(description = "ID of the ride", required = true)
            @PathVariable Long rideId,
            @Parameter(description = "Pagination offset", required = false, example = "0")
            @RequestParam(defaultValue = "0") @Min(0) Integer offset,
            @Parameter(description = "Pagination limit", required = false, example = "10")
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer limit
    );

    @Operation(summary = "Delete a rating for ride from the passenger",
            description = "Deletes the rating for ride from the passenger by rating's ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Rating deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Rating not found")
    })
    @DeleteMapping("/passenger-ratings/{id}")
    void deletePassengerRating(
            @Parameter(description = "ID of the rating to delete", required = true)
            @PathVariable Long id
    );

}