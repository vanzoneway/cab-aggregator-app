package com.modsen.ratingservice.controller.general;

import com.modsen.ratingservice.dto.ListContainerResponseDto;
import com.modsen.ratingservice.dto.Marker;
import com.modsen.ratingservice.dto.request.RatingRequestDto;
import com.modsen.ratingservice.dto.response.AverageRatingResponseDto;
import com.modsen.ratingservice.dto.response.RatingResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Validated
@Tag(name = "passenger-rating-operations", description = """
        The endpoints contained here are intended for operations related to passenger ratings. For example: creating a rating,
        updating a rating, retrieving a rating, deleting a rating, and calculating the average rating for a passenger.

        It is important to note that JWT authorization is used here: ROLE_ADMIN can perform all actions, while
        ROLE_DRIVER can only perform actions related to themselves (they cannot modify ratings of other passengers).

        Additionally, please be aware that when creating or updating a rating, the data is validated against
        the ride service to ensure the ride exists and is associated with the correct passenger.
        """)
@SecurityRequirement(name = "bearerAuth")
public interface PassengerRatingOperations {

    @Operation(summary = "Creates a new passenger rating",
            description = """
                    Creates a new rating for a passenger. Only accessible by ADMIN or the DRIVER themselves.

                    Required fields:
                    - **comment**: Comment about the rating (non-empty string)
                    - **rating**: Rating score (integer from 1 to 5)
                    - **rideId**: ID of the ride associated with the rating

                    Validation rules:
                    - `comment` must not be blank.
                    - `rating` must be an integer between 1 and 5.
                    - `rideId` must not be null and must correspond to an existing ride.

                    Example for creating a rating:
                    {
                        "comment": "Great passenger!",
                        "rating": 5,
                        "rideId": 1001
                    }

                    Example of a rating object after creation:
                    {
                        "id": 1,
                        "comment": "Great passenger!",
                        "rating": 5,
                        "rideId": 1001,
                        "refUserId": 1,
                        "userType": "PASSENGER"
                    }
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Passenger rating created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden, only ADMIN or DRIVER can access this endpoint"),
            @ApiResponse(responseCode = "404", description = "Ride not found"),
            @ApiResponse(responseCode = "409", description = "Conflict, duplicate ride ID")
    })
    @Validated(Marker.OnCreate.class)
    RatingResponseDto createPassengerRating(
            @RequestBody @Valid RatingRequestDto ratingRequestDto);

    @Operation(summary = "Updates an existing passenger rating",
            description = """
                    Updates an existing rating for a passenger. Only accessible by ADMIN.

                    Required fields:
                    - **comment**: Updated comment about the rating
                    - **rating**: Updated rating score (integer from 1 to 5)

                    Validation rules:
                    - `comment` must not be blank.
                    - `rating` must be an integer between 1 and 5.

                    Example for updating a rating:
                    {
                        "comment": "Updated comment",
                        "rating": 4
                    }

                    Example of a rating object after update:
                    {
                        "id": 1,
                        "comment": "Updated comment",
                        "rating": 4,
                        "rideId": 1001,
                        "refUserId": 1,
                        "userType": "PASSENGER"
                    }
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Passenger rating updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden, only ADMIN can access this endpoint"),
            @ApiResponse(responseCode = "404", description = "Rating not found"),
            @ApiResponse(responseCode = "409", description = "Conflict, duplicate ride ID")
    })
    @Validated(Marker.OnUpdate.class)
    RatingResponseDto updatePassengerRating(
            @PathVariable Long id,
            @RequestBody @Valid RatingRequestDto ratingRequestDto);

    @Operation(summary = "Retrieves a passenger rating by ID",
            description = """
                    Retrieves the details of a specific passenger rating.

                    Example request:
                    GET /api/v1/passengers-ratings/1

                    Example response:
                    {
                        "id": 1,
                        "comment": "Great passenger!",
                        "rating": 5,
                        "rideId": 1001,
                        "refUserId": 1,
                        "userType": "PASSENGER"
                    }
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Passenger rating retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Rating not found")
    })
    RatingResponseDto getPassengerRating(
            @PathVariable Long id);

    @Operation(summary = "Retrieves a paginated list of passenger ratings by user ID",
            description = """
                    Retrieves a paginated list of passenger ratings for a specific user. The response includes metadata such as:
                    - **currentOffset**: The current offset in the list.
                    - **currentLimit**: The number of items per page.
                    - **totalPages**: The total number of pages.
                    - **totalElements**: The total number of ratings.
                    - **sort**: The sorting criteria (if any).
                    - **values**: The list of ratings.

                    Example response:
                    {
                        "currentOffset": 0,
                        "currentLimit": 10,
                        "totalPages": 5,
                        "totalElements": 50,
                        "sort": "id,asc",
                        "values": [
                            {
                                "id": 1,
                                "comment": "Great passenger!",
                                "rating": 5,
                                "rideId": 1001,
                                "refUserId": 1,
                                "userType": "PASSENGER"
                            },
                            ...
                        ]
                    }
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Passenger ratings retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    ListContainerResponseDto<RatingResponseDto> getPassengerRatingsByRefUserId(
            @PathVariable Long refUserId,
            @RequestParam(defaultValue = "0") @Min(0) Integer offset,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer limit);

    @Operation(summary = "Deletes a passenger rating by ID",
            description = """
                    Deletes a specific passenger rating. Only accessible by ADMIN.

                    Example request:
                    DELETE /api/v1/passengers-ratings/1

                    Example response:
                    - Status Code: 204 (No Content)
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Passenger rating deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden, only ADMIN can access this endpoint"),
            @ApiResponse(responseCode = "404", description = "Rating not found")
    })
    void deletePassengerRating(
            @PathVariable Long id);

    @Operation(summary = "Retrieves the average rating for a passenger",
            description = """
                    Calculates and retrieves the average rating for a specific passenger.

                    Example request:
                    GET /api/v1/passengers-ratings/1/average

                    Example response:
                    {
                        "refUserId": 1,
                        "averageRating": 4.5
                    }
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Average rating retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Passenger not found")
    })
    AverageRatingResponseDto averagePassengerRating(
            @PathVariable Long refUserId);

}