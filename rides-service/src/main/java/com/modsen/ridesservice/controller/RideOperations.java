package com.modsen.ridesservice.controller;

import com.modsen.ridesservice.dto.ListContainerResponseDto;
import com.modsen.ridesservice.dto.Marker;
import com.modsen.ridesservice.dto.request.RideRequestDto;
import com.modsen.ridesservice.dto.request.RideStatusRequestDto;
import com.modsen.ridesservice.dto.response.RideResponseDto;
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

@Validated
@Tag(name = "Ride controller", description = """
         This controller contains endpoints for creating rides, updating ride information,\s
         changing ride status, and retrieving rides based on various criteria.
        \s""")
public interface RideOperations {

    @Operation(summary = "Create a new ride",
            description = """
                    Details of the ride to be created. Required fields:\s
                    - **driverId**: Id of the driver from other service
                    - **passengerId**: Id of the passenger from other service
                    - **departureAddress**: Departure address (non-empty string)
                    - **destinationAddress**: Destination address (non-empty string)
                    Example:\s
                    {
                      "driverId": "1",
                      "passengerId": "2",
                      "departureAddress": "123 Main St",
                      "destinationAddress": "456 Elm St",
                    }""")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ride created successfully"),
            @ApiResponse(responseCode = "400", description = "When one of the required fields is missing in the body" +
                    " or when the parameters in the request are incorrect.")
    })
    @Validated(Marker.OnCreate.class)
    RideResponseDto createRide(
            @Parameter(description = "Ride details to be created.", required = true) @RequestBody @Valid
            RideRequestDto rideRequestDto);

    @Operation(summary = "Get a paginated list of rides",
            description = "Retrieves a paginated list of rides based on offset and limit.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of rides retrieved successfully")
    })
    ListContainerResponseDto<RideResponseDto> getPageRides(
            @Parameter(description = "Pagination offset", example = "0") @RequestParam(defaultValue = "0")
            @Min(0)
            Integer offset,
            @Parameter(description = "Pagination limit", example = "10") @RequestParam(defaultValue = "10")
            @Min(1) @Max(100)
            Integer limit);

    @Operation(summary = "Change the status of a ride",
            description = "Updates the status of the specified ride.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ride status changed successfully"),
            @ApiResponse(responseCode = "404", description = "Ride not found"),
            @ApiResponse(responseCode = "400", description = "When the request body is invalid.")
    })
    RideResponseDto changeRideStatus(
            @Parameter(description = "ID of the ride", required = true) @PathVariable
            Long rideId,
            @Parameter(description = "New status for the ride", required = true) @RequestBody @Valid
            RideStatusRequestDto rideStatusRequestDto);

    @Operation(summary = "Update ride information",
            description = "Updates the ride details for the specified ride ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ride updated successfully"),
            @ApiResponse(responseCode = "404", description = "Ride not found"),
            @ApiResponse(responseCode = "400", description = "When one of the required fields is missing in the body.")
    })
    @Validated(Marker.OnUpdate.class)
    RideResponseDto updateRide(
            @Parameter(description = "ID of the ride", required = true) @PathVariable
            Long rideId,
            @Parameter(description = "Updated ride details", required = true) @RequestBody @Valid
            RideRequestDto rideRequestDto);

    @Operation(summary = "Get a ride by its ID",
            description = "Retrieves the details of a specified ride using its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ride retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Ride not found")
    })
    RideResponseDto getRideById(
            @Parameter(description = "ID of the ride", required = true) @PathVariable
            Long rideId);

    @Operation(summary = "Get a paginated list of rides by driver ID",
            description = "Retrieves a paginated list of rides for a specific driver based on offset and limit.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of rides retrieved successfully"),
    })
    ListContainerResponseDto<RideResponseDto> getPageRidesByDriverId(
            @Parameter(description = "ID of the driver", example = "5")
            @PathVariable
            Long driverId,
            @Parameter(description = "Pagination offset", example = "0")
            @RequestParam(defaultValue = "0")
            @Min(0)
            Integer offset,
            @Parameter(description = "Pagination limit", example = "10")
            @RequestParam(defaultValue = "10")
            @Min(1) @Max(100)
            Integer limit);


    @Operation(summary = "Get a paginated list of rides by passenger ID",
            description = "Retrieves a paginated list of rides for a specific passenger based on offset and limit.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of rides retrieved successfully"),
    })
    ListContainerResponseDto<RideResponseDto> getPageRidesByPassengerId(
            @Parameter(description = "ID of the passenger", example = "7")
            @PathVariable
            Long passengerId,
            @Parameter(description = "Pagination offset", example = "0")
            @RequestParam(defaultValue = "0")
            @Min(0)
            Integer offset,
            @Parameter(description = "Pagination limit", example = "10")
            @RequestParam(defaultValue = "10")
            @Min(1) @Max(100)
            Integer limit);

}
