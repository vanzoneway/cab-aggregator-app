package com.modsen.driverservice.controller;

import com.modsen.driverservice.dto.DriverDto;
import com.modsen.driverservice.dto.Marker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;



@Validated
@Tag(name = "Driver controller", description = """
         This controller contains endpoints for creating a driver, updating driver information,\s
         performing a soft delete of a driver, and retrieving information\s
         about all drivers.
        \s""")
public interface DriverOperations {

    @Operation(summary = "Create a new driver",
            description = """
                    Required fields:\s
                    - **name**: Name of the driver (non-empty string)
                    - **email**: Valid email address
                    - **phone**: Phone number in valid format
                    - **age**: Age of the driver (must be 21 or older)
                    - **gender**: Gender of the driver (non-empty string)
                    Example:\s
                    {
                      "name": "Jane Doe",
                      "email": "janedoe@example.com",
                      "phone": "+1234567890",
                      "age": 30,
                      "gender": "Female"
                    }""")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Driver created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided"),
            @ApiResponse(responseCode = "409", description = "A conflict occurred. This happens when such a driver " +
                    "email or phone already exists")
    })
    @PostMapping("/drivers")
    @Validated(Marker.OnCreate.class)
    DriverDto createDriver(
            @Parameter(description = "Driver details to be created", required = true)
            @RequestBody @Valid DriverDto driverDto);


    @Operation(summary = "Get paginated list of drivers",
            description = "Retrieves a paginated list of drivers.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Drivers retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    @GetMapping("/drivers")
    Page<DriverDto> getPageDrivers(
            @Parameter(description = "Offset for pagination", example = "0")
            @RequestParam(defaultValue = "0") @Min(0) Integer offset,
            @Parameter(description = "Limit for pagination", example = "10")
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer limit);


    @Operation(summary = "Soft delete a driver",
            description = "Marks the specified driver as deleted.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Driver deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Driver not found")
    })
    @DeleteMapping("/drivers/{driverId}")
    ResponseEntity<String> safeDeleteDriver(
            @Parameter(description = "ID of the driver to be deleted", required = true)
            @PathVariable Long driverId);


    @Operation(summary = "Update driver information",
            description = "Updates the details of the specified driver.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Driver updated successfully"),
            @ApiResponse(responseCode = "404", description = "Driver not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided"),
            @ApiResponse(responseCode = "409", description = "A conflict occurred. This happens when such a driver " +
                    "email or phone already exists")
    })
    @PutMapping("/drivers/{driverId}")
    DriverDto updateDriverById(
            @Parameter(description = "ID of the driver to be updated", required = true)
            @PathVariable Long driverId,
            @Parameter(description = "Updated driver details. It is not necessary to specify all parameters. It is " +
                    "sufficient to provide just a subset of them.",
                    required = true)
            @RequestBody @Valid DriverDto driverDto);
}

