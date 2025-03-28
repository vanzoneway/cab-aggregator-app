package com.modsen.ridesservice.controller;

import com.modsen.ridesservice.dto.ListContainerResponseDto;
import com.modsen.ridesservice.dto.Marker;
import com.modsen.ridesservice.dto.request.RideRequestDto;
import com.modsen.ridesservice.dto.request.RideStatusRequestDto;
import com.modsen.ridesservice.dto.response.RideResponseDto;
import com.modsen.ridesservice.dto.response.RideStatisticResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@Tag(name = "ride-operations", description = """
        The endpoints contained here are intended for operations related to rides. For example: creating a ride,
        updating ride information, changing ride status, retrieving rides by ID, and retrieving paginated lists of rides
        for drivers or passengers.

        It is important to note that JWT authorization is used here: ROLE_ADMIN can perform all actions, while
        ROLE_DRIVER and ROLE_PASSENGER can only perform actions related to themselves.
        """)
@SecurityRequirement(name = "bearerAuth")
public interface RideOperations {

    @Operation(summary = "Creates a new ride",
            description = """
                    Creates a new ride with the provided details. Only accessible by ADMIN or PASSENGER.

                    Required fields:
                    - **driverId**: ID of the driver (must exist in the system)
                    - **passengerId**: ID of the passenger (must exist in the system)
                    - **departureAddress**: Departure address (non-empty string)
                    - **destinationAddress**: Destination address (non-empty string)

                    Validation rules:
                    - `driverId` and `passengerId` must not be null.
                    - `departureAddress` and `destinationAddress` must not be blank.

                    Example request:
                    {
                        "driverId": 1,
                        "passengerId": 2,
                        "departureAddress": "123 Main St",
                        "destinationAddress": "456 Elm St"
                    }

                    Example response:
                    {
                        "id": 1,
                        "driverId": 1,
                        "passengerId": 2,
                        "departureAddress": "123 Main St",
                        "destinationAddress": "456 Elm St",
                        "rideStatus": "CREATED",
                        "orderDateTime": "2023-10-01T12:00:00",
                        "cost": 25.50
                    }
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ride created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden, only ADMIN or PASSENGER can access this endpoint"),
            @ApiResponse(responseCode = "404", description = "Driver or passenger not found")
    })
    @Validated(Marker.OnCreate.class)
    RideResponseDto createRide(
            @Parameter(description = "Ride details to be created", required = true)
            @RequestBody @Valid RideRequestDto rideRequestDto);

    @Operation(summary = "Retrieves a paginated list of rides",
            description = """
                    Retrieves a paginated list of rides. The response includes metadata such as:
                    - **currentOffset**: The current offset in the list.
                    - **currentLimit**: The number of items per page.
                    - **totalPages**: The total number of pages.
                    - **totalElements**: The total number of rides.
                    - **sort**: The sorting criteria (if any).
                    - **values**: The list of rides.

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
                                "driverId": 1,
                                "passengerId": 2,
                                "departureAddress": "123 Main St",
                                "destinationAddress": "456 Elm St",
                                "rideStatus": "CREATED",
                                "orderDateTime": "2023-10-01T12:00:00",
                                "cost": 25.50
                            },
                            ...
                        ]
                    }
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of rides retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    ListContainerResponseDto<RideResponseDto> getPageRides(
            @Parameter(description = "Pagination offset", example = "0")
            @RequestParam(defaultValue = "0") @Min(0) Integer offset,
            @Parameter(description = "Pagination limit", example = "10")
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer limit);

    @Operation(summary = "Changes the status of a ride",
            description = """
                    Changes the status of a specific ride. Only accessible by ADMIN, DRIVER, or PASSENGER.

                    Required fields:
                    - **rideStatus**: New status for the ride (must be a valid status: CREATED, IN_PROGRESS, COMPLETED, CANCELED)

                    Validation rules:
                    - `rideStatus` must not be null and must be a valid status.

                    Example request:
                    {
                        "rideStatus": "IN_PROGRESS"
                    }

                    Example response:
                    {
                        "id": 1,
                        "driverId": 1,
                        "passengerId": 2,
                        "departureAddress": "123 Main St",
                        "destinationAddress": "456 Elm St",
                        "rideStatus": "IN_PROGRESS",
                        "orderDateTime": "2023-10-01T12:00:00",
                        "cost": 25.50
                    }
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ride status changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden, only ADMIN, DRIVER, or PASSENGER can access this endpoint"),
            @ApiResponse(responseCode = "404", description = "Ride not found")
    })
    RideResponseDto changeRideStatus(
            @Parameter(description = "ID of the ride", required = true)
            @PathVariable Long rideId,
            @Parameter(description = "New status for the ride", required = true)
            @RequestBody @Valid RideStatusRequestDto rideStatusRequestDto);

    @Operation(summary = "Updates ride information",
            description = """
                    Updates the details of a specific ride. Only accessible by ADMIN.

                    Required fields:
                    - **driverId**: ID of the driver (must exist in the system)
                    - **passengerId**: ID of the passenger (must exist in the system)
                    - **departureAddress**: Departure address (non-empty string)
                    - **destinationAddress**: Destination address (non-empty string)

                    Validation rules:
                    - `driverId` and `passengerId` must not be null.
                    - `departureAddress` and `destinationAddress` must not be blank.

                    Example request:
                    {
                        "driverId": 1,
                        "passengerId": 2,
                        "departureAddress": "123 Main St",
                        "destinationAddress": "456 Elm St"
                    }

                    Example response:
                    {
                        "id": 1,
                        "driverId": 1,
                        "passengerId": 2,
                        "departureAddress": "123 Main St",
                        "destinationAddress": "456 Elm St",
                        "rideStatus": "CREATED",
                        "orderDateTime": "2023-10-01T12:00:00",
                        "cost": 25.50
                    }
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ride updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden, only ADMIN can access this endpoint"),
            @ApiResponse(responseCode = "404", description = "Ride not found")
    })
    @Validated(Marker.OnUpdate.class)
    RideResponseDto updateRide(
            @Parameter(description = "ID of the ride", required = true)
            @PathVariable Long rideId,
            @Parameter(description = "Updated ride details", required = true)
            @RequestBody @Valid RideRequestDto rideRequestDto);

    @Operation(summary = "Retrieves a ride by ID",
            description = """
                    Retrieves the details of a specific ride by its ID.

                    Example request:
                    GET /api/v1/rides/1

                    Example response:
                    {
                        "id": 1,
                        "driverId": 1,
                        "passengerId": 2,
                        "departureAddress": "123 Main St",
                        "destinationAddress": "456 Elm St",
                        "rideStatus": "CREATED",
                        "orderDateTime": "2023-10-01T12:00:00",
                        "cost": 25.50
                    }
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ride retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Ride not found")
    })
    RideResponseDto getRideById(
            @Parameter(description = "ID of the ride", required = true)
            @PathVariable Long rideId);

    @Operation(summary = "Retrieves a paginated list of rides by driver ID",
            description = """
                    Retrieves a paginated list of rides for a specific driver. The response includes metadata such as:
                    - **currentOffset**: The current offset in the list.
                    - **currentLimit**: The number of items per page.
                    - **totalPages**: The total number of pages.
                    - **totalElements**: The total number of rides.
                    - **sort**: The sorting criteria (if any).
                    - **values**: The list of rides.

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
                                "driverId": 1,
                                "passengerId": 2,
                                "departureAddress": "123 Main St",
                                "destinationAddress": "456 Elm St",
                                "rideStatus": "CREATED",
                                "orderDateTime": "2023-10-01T12:00:00",
                                "cost": 25.50
                            },
                            ...
                        ]
                    }
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of rides retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    ListContainerResponseDto<RideResponseDto> getPageRidesByDriverId(
            @Parameter(description = "ID of the driver", example = "1")
            @PathVariable Long driverId,
            @Parameter(description = "Pagination offset", example = "0")
            @RequestParam(defaultValue = "0") @Min(0) Integer offset,
            @Parameter(description = "Pagination limit", example = "10")
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer limit);

    @Operation(summary = "Retrieves a paginated list of rides by passenger ID",
            description = """
                    Retrieves a paginated list of rides for a specific passenger. The response includes metadata such as:
                    - **currentOffset**: The current offset in the list.
                    - **currentLimit**: The number of items per page.
                    - **totalPages**: The total number of pages.
                    - **totalElements**: The total number of rides.
                    - **sort**: The sorting criteria (if any).
                    - **values**: The list of rides.

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
                                "driverId": 1,
                                "passengerId": 2,
                                "departureAddress": "123 Main St",
                                "destinationAddress": "456 Elm St",
                                "rideStatus": "CREATED",
                                "orderDateTime": "2023-10-01T12:00:00",
                                "cost": 25.50
                            },
                            ...
                        ]
                    }
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of rides retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    ListContainerResponseDto<RideResponseDto> getPageRidesByPassengerId(
            @Parameter(description = "ID of the passenger", example = "2")
            @PathVariable Long passengerId,
            @Parameter(description = "Pagination offset", example = "0")
            @RequestParam(defaultValue = "0") @Min(0) Integer offset,
            @Parameter(description = "Pagination limit", example = "10")
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer limit);

    @Operation(summary = "Retrieves ride statistics for a passenger",
            description = """
                    Retrieves ride statistics for a specific passenger, including the average cost of rides and a list of rides.

                    Example request:
                    GET /api/v1/rides/passengers/2/statistics

                    Example response:
                    {
                        "userRefId": 2,
                        "averageCost": 25.50,
                        "rides": [
                            {
                                "id": 1,
                                "driverId": 1,
                                "passengerId": 2,
                                "departureAddress": "123 Main St",
                                "destinationAddress": "456 Elm St",
                                "rideStatus": "CREATED",
                                "orderDateTime": "2023-10-01T12:00:00",
                                "cost": 25.50
                            },
                            ...
                        ]
                    }
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ride statistics retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Passenger not found")
    })
    RideStatisticResponseDto getRideStatisticForPassenger(
            @Parameter(description = "ID of the passenger", required = true)
            @PathVariable Long passengerId);

    @Operation(summary = "Retrieves ride statistics for a driver",
            description = """
                    Retrieves ride statistics for a specific driver, including the average cost of rides and a list of rides.

                    Example request:
                    GET /api/v1/rides/drivers/1/statistics

                    Example response:
                    {
                        "userRefId": 1,
                        "averageCost": 25.50,
                        "rides": [
                            {
                                "id": 1,
                                "driverId": 1,
                                "passengerId": 2,
                                "departureAddress": "123 Main St",
                                "destinationAddress": "456 Elm St",
                                "rideStatus": "CREATED",
                                "orderDateTime": "2023-10-01T12:00:00",
                                "cost": 25.50
                            },
                            ...
                        ]
                    }
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ride statistics retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Driver not found")
    })
    RideStatisticResponseDto getRideStatisticForDriver(
            @Parameter(description = "ID of the driver", required = true)
            @PathVariable Long driverId);

}