package com.modsen.driverservice.controller;

import com.modsen.driverservice.dto.CarDto;
import com.modsen.driverservice.dto.Marker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Validated
@Tag(name = "car-operations", description = """
        The endpoints contained here are intended for operations related to cars. For example: creating a car,
        updating a car, deleting a car, and retrieving a specific car by ID.

        It is important to note that JWT authorization is used here: only ROLE_ADMIN can perform all actions.
        """)
@SecurityRequirement(name = "bearerAuth")
public interface CarOperations {

    @Operation(summary = "Creates a new car for a driver",
            description = """
                    Creates a new car for a specific driver. Only accessible by ADMIN.

                    Required fields:
                    - **brand**: Brand of the car (non-empty string)
                    - **color**: Color of the car (non-empty string)
                    - **number**: License plate number of the car (must match the pattern: 3 uppercase letters followed by 3 digits, e.g., ABC123)
                    - **model**: Model of the car (non-empty string)
                    - **year**: Manufacturing year of the car (non-null integer)

                    Validation rules:
                    - `brand`, `color`, `model`, and `number` must not be blank.
                    - `year` must not be null.
                    - `number` must match the pattern: ^[A-Z]{3}[0-9]{3}$.

                    Example for creating a car:
                    {
                        "brand": "Toyota",
                        "color": "Red",
                        "number": "ABC123",
                        "model": "Camry",
                        "year": 2020
                    }

                    Example of a car object after creation:
                    {
                        "id": 1,
                        "brand": "Toyota",
                        "color": "Red",
                        "number": "ABC123",
                        "model": "Camry",
                        "year": 2020,
                        "deleted": false
                    }
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Car created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden, only ADMIN can access this endpoint"),
            @ApiResponse(responseCode = "404", description = "Driver not found")
    })
    @Validated(Marker.OnCreate.class)
    CarDto createCar(
            @PathVariable Long driverId,
            @RequestBody @Valid CarDto carDto);

    @Operation(summary = "Updates a car by ID and driver ID",
            description = """
                    Updates a car's details by its ID and the driver's ID. Only accessible by ADMIN.

                    Example request:
                    PUT /api/v1/cars/1/drivers/34
                    {
                        "color": "Blue"
                    }

                    Example response:
                    {
                        "id": 1,
                        "brand": "Toyota",
                        "color": "Blue",
                        "number": "ABC123",
                        "model": "Camry",
                        "year": 2020,
                        "deleted": false
                    }
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden, only ADMIN can access this endpoint"),
            @ApiResponse(responseCode = "404", description = "Car or driver not found")
    })
    @Validated(Marker.OnUpdate.class)
    CarDto updateCarByCarIdAndDriverId(
            @PathVariable Long carId,
            @PathVariable Long driverId,
            @RequestBody @Valid CarDto carDto);

    @Operation(summary = "Deletes a car by ID",
            description = """
                    Deletes a car by its ID. Only accessible by ADMIN.

                    Example request:
                    DELETE /api/v1/cars/1

                    Example response:
                    - Status Code: 204 (No Content)
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Car deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden, only ADMIN can access this endpoint"),
            @ApiResponse(responseCode = "404", description = "Car not found")
    })
    void safeDeleteCarById(@PathVariable Long carId);

    @Operation(summary = "Retrieves a car by ID",
            description = """
                    Retrieves a car's details by its ID.

                    Example request:
                    GET /api/v1/cars/1

                    Example response:
                    {
                        "id": 1,
                        "brand": "Toyota",
                        "color": "Red",
                        "number": "ABC123",
                        "model": "Camry",
                        "year": 2020,
                        "deleted": false
                    }
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved car details"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Car not found")
    })
    CarDto getCarById(@PathVariable Long carId);

}
