package com.modsen.driverservice.controller;

import com.modsen.driverservice.dto.CarDto;
import com.modsen.driverservice.dto.DriverCarDto;
import com.modsen.driverservice.dto.Marker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Validated
@Tag(name = "Car controller", description = """
         This controller contains endpoints for creating a car for a specific driver, updating car information\s
         for a specific driver, performing a soft delete of a car, and retrieving information\s
         about the driver along with all cars associated with them.
        \s""")
public interface CarOperations {

    @Operation(summary = "Create a new car for a specific driver",
            description = """
                    Car details to be created. Required fields:\s
                    - **brand**: Brand of the car (non-empty string)
                    - **color**: Color of the car (non-empty string)
                    - **number**: Unique registration number of the car (valid format)
                    - **model**: Model of the car (non-empty string)
                    - **year**: Year the car was manufactured (integer value)
                    Example:\s
                    {
                      "brand": "Toyota",
                      "color": "Red",
                      "number": "AB123CD",
                      "model": "Camry",
                      "year": 2020,
                    }""")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car created successfully"),
            @ApiResponse(responseCode = "400", description = "When one of the required fields is missing in the body" +
                    " or when the parameters in the request are incorrect."),
            @ApiResponse(responseCode = "409", description = "A conflict occurred. This happens when such a car " +
                    "number already exists.")
    })
    @Validated(Marker.OnCreate.class)
    CarDto createCar(
            @Parameter(description = "ID of the driver", required = true) @PathVariable Long driverId,
            @Parameter(description = "Car details to be created.",
                    required = true)
            @RequestBody @Valid CarDto carDto);

    @Operation(summary = "Update car information for a specific driver",
            description = "Updates the car details for the specified driver and car ID."
    + " It is not necessary to specify all parameters. It is sufficient to provide just a subset of them.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car updated successfully"),
            @ApiResponse(responseCode = "404", description = "Car or driver not found"),
            @ApiResponse(responseCode = "400", description = "When one of the required fields is missing in the" +
                    " body or when the parameters in the request are incorrect."),
            @ApiResponse(responseCode = "409", description = "A conflict occurred. This happens when such a car" +
                    " number already exists.")
    })
    @Validated(Marker.OnUpdate.class)
    CarDto updateCarByCarIdAndDriverId(
            @Parameter(description = "ID of the car", required = true) @PathVariable Long carId,
            @Parameter(description = "ID of the driver", required = true) @PathVariable Long driverId,
            @Parameter(description = "CarDto or body for this PUT endpoint", required = true)
            @RequestBody @Valid CarDto carDto);

    @Operation(summary = "Soft delete a car by its ID",
            description = "Marks the specified car as deleted without removing it from the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Car deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Car not found")
    })
    void safeDeleteCarById(@Parameter(description = "ID of the car", required = true) @PathVariable Long carId);

    @Operation(summary = "Get a car by its ID",
            description = "Retrieves the details of a specified car using its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Car not found")
    })
    CarDto getCarById(@Parameter(description = "ID of the car", required = true) @PathVariable Long carId);
}
