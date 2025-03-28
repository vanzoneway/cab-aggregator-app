package com.modsen.passengerservice.controller.general;

import com.modsen.passengerservice.dto.ListContainerResponseDto;
import com.modsen.passengerservice.dto.Marker;
import com.modsen.passengerservice.dto.PassengerDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Validated
@Tag(name = "passenger-operations", description = """
        The endpoints contained here are intended for operations related to passengers. For example: creating a passenger,
        retrieving a list of passengers, deleting passengers, updating a passenger by ID, and retrieving a specific passenger
        by ID.

        It is important to note that JWT authorization is used here: ROLE_ADMIN can perform all actions, while
        ROLE_PASSENGER can only perform actions related to themselves (they cannot modify information of other passengers).

        Additionally, please be aware that when deleting or updating a passenger, the data is not synchronized
        with the registration service database (THIS HAS NOT BEEN FIXED YET). This should be taken into account.
        """)
@SecurityRequirement(name = "bearerAuth")
public interface PassengerOperations {

    @Operation(summary = "Creates a new passenger",
            description = """
                    The passenger details to be created. Please note that the data is not yet synchronized
                    with the registration service database. Required fields:
                    - **firstName**: First name of the passenger (non-empty string)
                    - **lastName**: Last name of the passenger (non-empty string)
                    - **email**: Email address of the passenger (valid format)
                    - **phone**: Phone number of the passenger (valid format)

                    Validation rules:
                    - `firstName` and `lastName` must not be blank.
                    - `email` must be a valid email format and not empty.
                    - `phone` must match the pattern: +[country code][number] (7-15 digits total).

                    Example for creating a passenger:
                    {
                        "firstName": "John",
                        "lastName": "Doe",
                        "email": "john.doe@example.com",
                        "phone": "+1234567890"
                    }

                    Example of a passenger object after creation:
                    {
                        "id": 1,
                        "firstName": "John",
                        "lastName": "Doe",
                        "email": "john.doe@example.com",
                        "phone": "+1234567890",
                        "averageRating": null,
                        "deleted": false
                    }
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Passenger created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden, only ADMIN can access this endpoint"),
            @ApiResponse(responseCode = "409", description = "Conflict, duplicate email or phone")
    })
    @Validated(Marker.OnCreate.class)
    PassengerDto createPassenger(@RequestBody @Valid PassengerDto passengerDto);

    @Operation(summary = "Retrieves a paginated list of passengers",
            description = """
                    Retrieves a paginated list of passengers. The response includes metadata such as:
                    - **currentOffset**: The current offset in the list.
                    - **currentLimit**: The number of items per page.
                    - **totalPages**: The total number of pages.
                    - **totalElements**: The total number of passengers.
                    - **sort**: The sorting criteria (if any).
                    - **values**: The list of passengers.

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
                                "firstName": "John",
                                "lastName": "Doe",
                                "email": "john.doe@example.com",
                                "phone": "+1234567890",
                                "averageRating": 4.5,
                                "deleted": false
                            },
                            ...
                        ]
                    }
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of passengers"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    ListContainerResponseDto<PassengerDto> getPagePassengers(
            @RequestParam(defaultValue = "0") @Min(0) Integer offset,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer limit);

    @Operation(summary = "Deletes a passenger by ID",
            description = """
                    Deletes a passenger by their ID. Only accessible by ADMIN or the passenger themselves.
                    Please note that the data is not synchronized with the registration service database.

                    Example request:
                    DELETE /api/v1/passengers/1

                    Example response:
                    - Status Code: 204 (No Content)
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Passenger deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden, only ADMIN or the passenger themselves can access this endpoint"),
            @ApiResponse(responseCode = "404", description = "Passenger not found")
    })
    void safeDeletePassenger(
            @PathVariable Long passengerId,
            JwtAuthenticationToken token);

    @Operation(summary = "Updates a passenger by ID",
            description = """
                    Updates a passenger's details by their ID. Only accessible by ADMIN or the passenger themselves.
                    Please note that the data is not synchronized with the registration service database.

                    Example request:
                    PUT /api/v1/passengers/1
                    {
                        "phone": "+9876543210"
                    }

                    Example response:
                    {
                        "id": 1,
                        "firstName": "John",
                        "lastName": "Doe",
                        "email": "john.doe@example.com",
                        "phone": "+9876543210",
                        "averageRating": null,
                        "deleted": false
                    }
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Passenger updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden, only ADMIN or the passenger themselves can access this endpoint"),
            @ApiResponse(responseCode = "404", description = "Passenger not found"),
            @ApiResponse(responseCode = "409", description = "Conflict, duplicate email or phone")
    })
    @Validated(Marker.OnUpdate.class)
    PassengerDto updatePassengerById(
            @PathVariable Long passengerId,
            @RequestBody @Valid PassengerDto passengerDto,
            JwtAuthenticationToken token);

    @Operation(summary = "Retrieves a passenger by ID",
            description = """
                    Retrieves a passenger's details by their ID.

                    Example request:
                    GET /api/v1/passengers/1

                    Example response:
                    {
                        "id": 1,
                        "firstName": "John",
                        "lastName": "Doe",
                        "email": "john.doe@example.com",
                        "phone": "+1234567890",
                        "averageRating": null,
                        "deleted": false
                    }
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved passenger details"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Passenger not found")
    })
    PassengerDto getPassengerById(@PathVariable Long passengerId);

}