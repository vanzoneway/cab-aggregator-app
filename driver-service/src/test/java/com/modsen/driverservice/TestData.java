package com.modsen.driverservice;

import com.modsen.driverservice.dto.CarDto;
import com.modsen.driverservice.dto.DriverCarDto;
import com.modsen.driverservice.dto.DriverDto;
import com.modsen.driverservice.dto.ListContainerResponseDto;
import com.modsen.driverservice.model.Car;
import com.modsen.driverservice.model.Driver;

import java.util.ArrayList;
import java.util.Collections;

public final class TestData {

    private TestData() {}

    public static final Long DRIVER_ID = 1L;
    public static final Long CAR_ID = 1L;

    public static final String CAR_ENDPOINT = "/api/v1/cars/drivers/{driverId}";
    public static final String CAR_UPDATE_ENDPOINT = "/api/v1/cars/{carId}/drivers/{driverId}";
    public static final String CAR_DELETE_ENDPOINT = "/api/v1/cars/{carId}";
    public static final String CAR_GET_ENDPOINT = "/api/v1/cars/{carId}";

    public static final String DRIVER_ENDPOINT = "/api/v1/drivers";
    public static final String DRIVER_UPDATE_ENDPOINT = "/api/v1/drivers/{driverId}";
    public static final String DRIVER_DELETE_ENDPOINT = "/api/v1/drivers/{driverId}";
    public static final String DRIVER_GET_ENDPOINT = "/api/v1/drivers/{driverId}";
    public static final String DRIVER_CARS_ENDPOINT = "/api/v1/drivers/{driverId}/cars";

    public static final CarDto CAR_RESPONSE_DTO = new CarDto(
            CAR_ID,
            "Brand",
            "Color",
            "ABC123",
            "Model",
            2020,
            false);

    public static final CarDto CAR_REQUEST_DTO = new CarDto(
            null,
            "Brand",
            "Color",
            "ABC123",
            "Model",
            2020,
            null);

    public static final CarDto INVALID_CAR_REQUEST_DTO = new CarDto(
            null,
            "Brand",
            "Color",
            "ABC123ABC123",
            "Model",
            2020,
            true
    );

    public static final DriverDto DRIVER_RESPONSE_DTO = new DriverDto(
            DRIVER_ID,
            "Name",
            "jane.doe@example.org",
            "6625550144",
            "Gender",
            5.0d,
            false);

    public static final DriverDto DRIVER_REQUEST_DTO = new DriverDto(
            null,
            "Name",
            "jane.doe@example.org",
            "6625550144",
            "Gender",
            null,
            false
    );

    public static final DriverDto INVALID_DRIVER_REQUEST_DTO = new DriverDto(
            null,
            "Name",
            "",
            "",
            "Gender",
            5d,
            true
    );

    public static final DriverCarDto DRIVER_CAR_RESPONSE_DTO = new DriverCarDto(
            1L,
            "Name",
            "jane.doe@example.org",
            "6625550144",
            "Gender",
            5.0d,
            Collections.singletonList(CAR_RESPONSE_DTO));

    public static final ListContainerResponseDto<DriverDto> PAGE_DRIVER_RESPONSE_DTO = new ListContainerResponseDto<>(
            1, 1, 1, 1L,
            "Sort", new ArrayList<>());

    public static final Driver DRIVER_ENTITY;

    public static final Car CAR_ENTITY;

    public static final CarDto CAR_DTO = new CarDto(
            1L,
            "Brand",
            "Color",
            "42",
            "Model",
            1,
            true);

    public static final DriverDto DRIVER_DTO = new DriverDto(
            1L,
            "Name",
            "jane.doe@example.org",
            "6625550144",
            "Gender",
            10.0d,
            true);

    static {

        DRIVER_ENTITY = new Driver();
        DRIVER_ENTITY.setAverageRating(10.0d);
        DRIVER_ENTITY.setCars(new ArrayList<>());
        DRIVER_ENTITY.setDeleted(true);
        DRIVER_ENTITY.setEmail("jane.doe@example.org");
        DRIVER_ENTITY.setGender("Gender");
        DRIVER_ENTITY.setId(1L);
        DRIVER_ENTITY.setName("Name");
        DRIVER_ENTITY.setPhone("6625550144");

        CAR_ENTITY = new Car();
        CAR_ENTITY.setBrand("Brand");
        CAR_ENTITY.setColor("Color");
        CAR_ENTITY.setDeleted(true);
        CAR_ENTITY.setDriver(DRIVER_ENTITY);
        CAR_ENTITY.setId(1L);
        CAR_ENTITY.setModel("Model");
        CAR_ENTITY.setNumber("42");
        CAR_ENTITY.setYear(1);

    }

}
