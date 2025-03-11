package com.modsen.driverservice.controller;

import com.modsen.driverservice.dto.DriverCarDto;
import com.modsen.driverservice.dto.DriverDto;
import com.modsen.driverservice.dto.ListContainerResponseDto;
import com.modsen.driverservice.dto.Marker;
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
@Tag(name = "driver-operations", description = """
        The endpoints contained here are intended for operations related to drivers. For example: creating a driver,
        retrieving a list of drivers, deleting drivers, updating a driver by ID, retrieving a specific driver
        by ID, and getting a driver along with their list of cars.

        It is important to note that JWT authorization is used here: ROLE_ADMIN can perform all actions, while
        ROLE_DRIVER can only perform actions related to themselves (they cannot modify information of other drivers).

        Additionally, please be aware that when deleting or updating a driver, the data is not synchronized
        with the registration service database (THIS HAS NOT BEEN FIXED YET). This should be taken into account.
        """)
@SecurityRequirement(name = "bearerAuth")
public interface DriverOperations {

    @Operation(summary = "Creates a new driver",
            description = """
                    The driver details to be created. Please note that the data is not yet synchronized
                    with the registration service database. Required fields:
                    - **firstName**: First name of the driver (non-empty string)
                    - **lastName**: Last name of the driver (non-empty string)
                    - **email**: Email address of the driver (valid format)
                    - **phone**: Phone number of the driver (valid format)
                    - **gender**: Gender of the driver (e.g., 'Male', 'Female')

                    Validation rules:
                    - `firstName` and `lastName` must not be blank.
                    - `email` must be a valid email format and not empty.
                    - `phone` must match the pattern: +[country code][number] (7-15 digits total).
                    - `gender` must not be blank.

                    Example for creating a driver:
                    {
                        "firstName": "John",
                        "lastName": "Lock",
                        "email": "johnddoe@example.com",
                        "phone": "+1234567503",
                        "gender": "Male"
                    }

                    Example of a driver object after creation:
                    {
                        "id": 34,
                        "firstName": "John",
                        "lastName": "Lock",
                        "email": "johnddoe@example.com",
                        "phone": "+1234567503",
                        "gender": "Male",
                        "averageRating": null,
                        "deleted": false
                    }
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Driver created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden, only ADMIN can access this endpoint")
    })
    @Validated(Marker.OnCreate.class)
    DriverDto createDriver(
            @RequestBody @Valid DriverDto driverDto);

    @Operation(summary = "Retrieves a paginated list of drivers",
            description = """
                    Retrieves a paginated list of drivers. The response includes metadata such as:
                    - **currentOffset**: The current offset in the list.
                    - **currentLimit**: The number of items per page.
                    - **totalPages**: The total number of pages.
                    - **totalElements**: The total number of drivers.
                    - **sort**: The sorting criteria (if any).
                    - **values**: The list of drivers.

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
                                "gender": "Male",
                                "averageRating": 4.5,
                                "deleted": false
                            },
                            ...
                        ]
                    }
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of drivers"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    ListContainerResponseDto<DriverDto> getPageDrivers(
            @RequestParam(defaultValue = "0") @Min(0) Integer offset,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer limit);

    @Operation(summary = "Retrieves a paginated list of drivers",
            description = """
                    Retrieves a paginated list of drivers. The response includes metadata such as:
                    - **currentOffset**: The current offset in the list.
                    - **currentLimit**: The number of items per page.
                    - **totalPages**: The total number of pages.
                    - **totalElements**: The total number of drivers.
                    - **sort**: The sorting criteria (if any).
                    - **values**: The list of drivers.

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
                                "gender": "Male",
                                "averageRating": 4.5,
                                "deleted": false
                            },
                            ...
                        ]
                    }
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of drivers"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    void safeDeleteDriver(
            @PathVariable Long driverId,
            JwtAuthenticationToken jwtAuthenticationToken);

    @Operation(summary = "Updates a driver by ID",
            description = """
                    Updates a driver's details by their ID. Only accessible by ADMIN or the driver themselves.
                    Please note that the data is not synchronized with the registration service database.

                    Example request:
                    PUT /api/v1/drivers/34
                    {
                        "phone": "+9876543210"
                    }

                    Example response:
                    {
                        "id": 34,
                        "firstName": "John",
                        "lastName": "Lock",
                        "email": "johnddoe@example.com",
                        "phone": "+9876543210",
                        "gender": "Male",
                        "averageRating": null,
                        "deleted": false
                    }
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Driver updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden, only ADMIN or the driver themselves can access this endpoint"),
            @ApiResponse(responseCode = "404", description = "Driver not found")
    })
    @Validated(Marker.OnUpdate.class)
    DriverDto updateDriverById(
            @PathVariable Long driverId,
            @RequestBody @Valid DriverDto driverDto,
            JwtAuthenticationToken jwtAuthenticationToken);

    @Operation(summary = "Retrieves a driver by ID",
            description = """
                    Retrieves a driver's details by their ID.

                    Example request:
                    GET /api/v1/drivers/34

                    Example response:
                    {
                        "id": 34,
                        "firstName": "John",
                        "lastName": "Lock",
                        "email": "johnddoe@example.com",
                        "phone": "+1234567503",
                        "gender": "Male",
                        "averageRating": null,
                        "deleted": false
                    }
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved driver details"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Driver not found")
    })
    DriverDto getDriverById(@PathVariable Long driverId);

    @Operation(summary = "Retrieves a driver with their cars",
              description = """
                    Retrieves a driver's details along with their list of cars.

                    Example request:
                    GET /api/v1/drivers/34/cars

                    Example response:
                    {
                        "id": 34,
                        "firstName": "John",
                        "lastName": "Lock",
                        "email": "johnddoe@example.com",
                        "phone": "+1234567503",
                        "gender": "Male",
                        "averageRating": null,
                        "cars": [
                            {
                                "id": 1,
                                "model": "Toyota Camry",
                                "year": 2020,
                                "licensePlate": "ABC123"
                            },
                            ...
                        ]
                    }
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved driver details with cars"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Driver not found")
    })
    DriverCarDto getDriverWithCars(@PathVariable Long driverId);

}

