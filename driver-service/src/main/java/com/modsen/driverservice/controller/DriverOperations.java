package com.modsen.driverservice.controller;

import com.modsen.driverservice.dto.DriverCarDto;
import com.modsen.driverservice.dto.DriverDto;
import com.modsen.driverservice.dto.ListContainerResponseDto;
import com.modsen.driverservice.dto.Marker;
import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "car-operations", description = """
        The endpoints contained here are intended for operations related to drivers. For example: creating a driver,
        retrieving a list of drivers, deleting drivers, updating a driver by ID, retrieving a specific driver
        by ID, and getting a driver along with their list of cars.

        It is important to note that JWT authorization is used here: ROLE_ADMIN can perform all actions, while
        ROLE_DRIVER can only perform actions related to themselves (they cannot modify information of other drivers).

        Additionally, please be aware that when deleting or updating a driver, the data is not synchronized
        with the registration service database (THIS HAS NOT BEEN FIXED YET). This should be taken into account.
        """)
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
    @Validated(Marker.OnCreate.class)
    DriverDto createDriver(
            @RequestBody @Valid DriverDto driverDto);

    @Operation(summary = "Retrieves a paginated list of drivers",
            description = """
                    Retrieves a list of drivers with pagination support. The results are returned in a paginated format.
                    Required query parameters:
                    - **offset**: The starting point of the results (default is 0, must be >= 0).
                    - **limit**: The maximum number of results to return (default is 10, must be between 1 and 100).

                    Example request:
                    GET http://localhost:8080/api/v1/drivers?offset=0&limit=2

                    Example response:
                    {
                        "currentOffset": 0,
                        "currentLimit": 2,
                        "totalPages": 11,
                        "totalElements": 21,
                        "sort": "UNSORTED",
                        "values": [
                            {
                                "id": 1,
                                "firstName": "John",
                                "lastName": "Doe",
                                "email": "johndoe@example.com",
                                "phone": "+1234567890",
                                "gender": "Male",
                                "averageRating": null,
                                "deleted": false
                            },
                            {
                                "id": 2,
                                "firstName": "Jane",
                                "lastName": "Smith",
                                "email": "janesmith@example.com",
                                "phone": "+1234567891",
                                "gender": "Female",
                                "averageRating": null,
                                "deleted": false
                            }
                        ]
                    }
                    """)
    ListContainerResponseDto<DriverDto> getPageDrivers(
            @RequestParam(defaultValue = "0") @Min(0) Integer offset,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer limit);

    @Operation(summary = "Safely deletes a driver",
            description = """
                    Safely deletes a driver by their ID. This operation ensures that the driver's data is marked as deleted 
                    without permanently removing it from the database. This allows for potential data recovery if needed.
                    
                    Required path variable:
                    - **driverId**: The ID of the driver to be deleted (must be a valid ID).

                    Authorization:
                    - This operation requires JWT authentication. Only users with the appropriate roles can perform this action.

                    Example request:
                    DELETE http://localhost:8080/api/v1/drivers/{driverId}

                    Example response:
                    - HTTP 204 No Content: Indicates that the driver was successfully deleted.
                    """)
    void safeDeleteDriver(
            @PathVariable Long driverId,
            JwtAuthenticationToken jwtAuthenticationToken);


    @Validated(Marker.OnUpdate.class)
    DriverDto updateDriverById(
            @PathVariable Long driverId,
            @RequestBody @Valid DriverDto driverDto,
            JwtAuthenticationToken jwtAuthenticationToken);

    DriverDto getDriverById(@PathVariable Long driverId);

    DriverCarDto getDriverWithCars(@PathVariable Long driverId);

}

