package com.modsen.passengerservice.controller.general;

import com.modsen.passengerservice.dto.ListContainerResponseDto;
import com.modsen.passengerservice.dto.Marker;
import com.modsen.passengerservice.dto.PassengerDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "Driver controller", description = """
         This controller contains endpoints for creating a passenger, updating passenger information,\s
         performing a soft delete of a passenger, and retrieving information\s
         about all passengers.
        \s""")
public interface PassengerOperations {

    @Operation(summary = "Create a new passenger",
            description = """
                    Required fields:\s
                    - **firstName**: Name of the driver (non-empty string)
                    - **lastName**: lastName of the driver (non-empty string)
                    - **email**: Valid email address
                    - **phone**: Phone number in valid format
                    Example:\s
                    {
                      "firstName": "Jane",
                      "lastName": "Doe",
                      "email": "janedoe@example.com",
                      "phone": "+1234567890",
                    }""")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Passenger created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided"),
            @ApiResponse(responseCode = "409", description = "A conflict occurred. This happens when such a passenger" +
                    " email or phone already exists")
    })
    @Validated(Marker.OnCreate.class)
    PassengerDto createPassenger(
            @Parameter(description = "Passenger details to be created", required = true)
            @RequestBody @Valid PassengerDto passengerDto);

    @Operation(summary = "Get paginated list of passengers",
            description = "Retrieves a paginated list of passengers.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Passengers retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    ListContainerResponseDto<PassengerDto> getPagePassengers(
            @Parameter(description = "Offset for pagination", example = "0")
            @RequestParam(defaultValue = "0") @Min(0) Integer offset,
            @Parameter(description = "Limit for pagination", example = "10")
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer limit);

    @Operation(summary = "Soft delete a passenger",
            description = "Marks the specified passenger as deleted.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Passenger deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Passenger not found")
    })
    void safeDeletePassenger(
            @Parameter(description = "ID of the passenger to be deleted", required = true)
            @PathVariable Long passengerId,
            JwtAuthenticationToken token);

    @Operation(summary = "Update passenger information",
            description = "Updates the details of the specified passenger.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Driver updated successfully"),
            @ApiResponse(responseCode = "404", description = "Driver not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided"),
            @ApiResponse(responseCode = "409", description = "A conflict occurred. This happens when such a passenger" +
                    " email or phone already exists")
    })
    @Validated(Marker.OnUpdate.class)
    PassengerDto updatePassengerById(
            @Parameter(description = "ID of the passenger to be updated", required = true)
            @PathVariable Long passengerId,
            @Parameter(description = "Updated passenger details. It is not necessary to specify all parameters." +
                    " It is sufficient to provide just a subset of them.",
                    required = true)
            @RequestBody @Valid PassengerDto passengerDto,
            JwtAuthenticationToken token);

    @Operation(summary = "Get passenger by id",
            description = "Getting information about passenger in JSON format")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Passenger got successfully"),
            @ApiResponse(responseCode = "404", description = "Passenger not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided"),

    })
    PassengerDto getPassengerById(@PathVariable Long passengerId);

}
