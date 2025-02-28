package com.modsen.driverservice;

import com.modsen.driverservice.dto.CarDto;
import com.modsen.driverservice.dto.DriverCarDto;
import com.modsen.driverservice.dto.DriverDto;
import com.modsen.driverservice.dto.ListContainerResponseDto;
import com.modsen.driverservice.model.Car;
import com.modsen.driverservice.model.Driver;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestData {

    public static final Long DRIVER_ID = 1L;
    public static final Long CAR_ID = 1L;

    private static final String CAR_RESPONSE_REQUEST_BRAND = "Brand";
    private static final String CAR_RESPONSE_REQUEST_MODEL = "Model";
    private static final String CAR_RESPONSE_REQUEST_COLOR = "Color";
    private static final String CAR_RESPONSE_REQUEST_NUMBER = "ABC123";
    private static final Integer CAR_RESPONSE_REQUEST_YEAR = 2020;

    private static final String INVALID_CAR_RESPONSE_REQUEST_NUMBER = "ABC123ABC123";

    private static final String DRIVER_RESPONSE_REQUEST_FIRST_NAME = "FirstName";
    private static final String DRIVER_RESPONSE_REQUEST_LAST_NAME = "LastName";
    private static final String DRIVER_RESPONSE_REQUEST_EMAIL = "jane.doe@example.org";
    private static final String DRIVER_RESPONSE_REQUEST_PHONE = "6625550144";
    private static final String DRIVER_RESPONSE_REQUEST_GENDER = "Gender";
    private static final Double DRIVER_RESPONSE_REQUEST_AVERAGE_RATING = 5.0d;


    public static final String CAR_ENDPOINT = "/api/v1/cars/drivers/{driverId}";
    public static final String CAR_UPDATE_ENDPOINT = "/api/v1/cars/{carId}/drivers/{driverId}";
    public static final String CAR_DELETE_ENDPOINT = "/api/v1/cars/{carId}";
    public static final String CAR_GET_ENDPOINT = "/api/v1/cars/{carId}";

    public static final String SECURITY_ADMIN_ROLE = "ROLE_ADMIN";

    public static final String DRIVER_ENDPOINT = "/api/v1/drivers";
    public static final String DRIVER_UPDATE_ENDPOINT = "/api/v1/drivers/{driverId}";
    public static final String DRIVER_DELETE_ENDPOINT = "/api/v1/drivers/{driverId}";
    public static final String DRIVER_GET_ENDPOINT = "/api/v1/drivers/{driverId}";
    public static final String DRIVER_CARS_ENDPOINT = "/api/v1/drivers/{driverId}/cars";

    public static final CarDto CAR_RESPONSE_DTO = CarDto.builder()
            .withId(CAR_ID)
            .withBrand(CAR_RESPONSE_REQUEST_BRAND)
            .withModel(CAR_RESPONSE_REQUEST_MODEL)
            .withColor(CAR_RESPONSE_REQUEST_COLOR)
            .withYear(CAR_RESPONSE_REQUEST_YEAR)
            .withNumber(CAR_RESPONSE_REQUEST_NUMBER)
            .withDeleted(false)
            .build();

    public static final CarDto CAR_REQUEST_DTO = CarDto.builder()
            .withId(null)
            .withBrand(CAR_RESPONSE_REQUEST_BRAND)
            .withModel(CAR_RESPONSE_REQUEST_MODEL)
            .withColor(CAR_RESPONSE_REQUEST_COLOR)
            .withYear(CAR_RESPONSE_REQUEST_YEAR)
            .withNumber(CAR_RESPONSE_REQUEST_NUMBER)
            .withDeleted(null)
            .build();

    public static final CarDto INVALID_CAR_REQUEST_DTO = CarDto.builder()
            .withId(null)
            .withBrand(CAR_RESPONSE_REQUEST_BRAND)
            .withModel(CAR_RESPONSE_REQUEST_MODEL)
            .withColor(CAR_RESPONSE_REQUEST_COLOR)
            .withYear(CAR_RESPONSE_REQUEST_YEAR)
            .withNumber(INVALID_CAR_RESPONSE_REQUEST_NUMBER)
            .withDeleted(true)
            .build();

    public static final DriverDto DRIVER_RESPONSE_DTO = DriverDto.builder()
            .withId(DRIVER_ID)
            .withFirstName(DRIVER_RESPONSE_REQUEST_FIRST_NAME)
            .withLastName(DRIVER_RESPONSE_REQUEST_LAST_NAME)
            .withEmail(DRIVER_RESPONSE_REQUEST_EMAIL)
            .withPhone(DRIVER_RESPONSE_REQUEST_PHONE)
            .withGender(DRIVER_RESPONSE_REQUEST_GENDER)
            .withAverageRating(DRIVER_RESPONSE_REQUEST_AVERAGE_RATING)
            .withDeleted(false)
            .build();

    public static final DriverDto DRIVER_REQUEST_DTO = DriverDto.builder()
            .withId(null)
            .withFirstName(DRIVER_RESPONSE_REQUEST_FIRST_NAME)
            .withLastName(DRIVER_RESPONSE_REQUEST_LAST_NAME)
            .withEmail(DRIVER_RESPONSE_REQUEST_EMAIL)
            .withPhone(DRIVER_RESPONSE_REQUEST_PHONE)
            .withGender(DRIVER_RESPONSE_REQUEST_GENDER)
            .withAverageRating(null)
            .withDeleted(false)
            .build();

    public static final DriverDto DRIVER_REQUEST_UPDATE_DTO = DriverDto.builder()
            .withId(null)
            .withFirstName(null)
            .withLastName(null)
            .withGender(null)
            .withEmail(null)
            .withPhone(DRIVER_RESPONSE_REQUEST_PHONE)
            .withAverageRating(null)
            .withDeleted(false)
            .build();

    public static final DriverDto INVALID_DRIVER_REQUEST_DTO = DriverDto.builder()
            .withId(null)
            .withFirstName(DRIVER_RESPONSE_REQUEST_FIRST_NAME)
            .withLastName(DRIVER_RESPONSE_REQUEST_LAST_NAME)
            .withEmail("")
            .withPhone("")
            .withGender(DRIVER_RESPONSE_REQUEST_GENDER)
            .withAverageRating(DRIVER_RESPONSE_REQUEST_AVERAGE_RATING)
            .withDeleted(true)
            .build();

    public static final DriverCarDto DRIVER_CAR_RESPONSE_DTO = DriverCarDto.builder()
            .withId(DRIVER_ID)
            .withFirstName(DRIVER_RESPONSE_REQUEST_FIRST_NAME)
            .withLastName(DRIVER_RESPONSE_REQUEST_LAST_NAME)
            .withEmail(DRIVER_RESPONSE_REQUEST_EMAIL)
            .withPhone(DRIVER_RESPONSE_REQUEST_PHONE)
            .withGender(DRIVER_RESPONSE_REQUEST_GENDER)
            .withAverageRating(DRIVER_RESPONSE_REQUEST_AVERAGE_RATING)
            .withCars(Collections.singletonList(CAR_RESPONSE_DTO))
            .build();

    public static final ListContainerResponseDto<DriverDto> PAGE_DRIVER_RESPONSE_DTO = new ListContainerResponseDto<>(
            1, 1, 1, 1L,
            "Sort", new ArrayList<>());

    public static final Driver DRIVER_ENTITY;

    public static final Car CAR_ENTITY;

    public static final CarDto CAR_DTO = CarDto.builder()
            .withId(CAR_ID)
            .withBrand(CAR_RESPONSE_REQUEST_BRAND)
            .withColor(CAR_RESPONSE_REQUEST_COLOR)
            .withYear(CAR_RESPONSE_REQUEST_YEAR)
            .withNumber(CAR_RESPONSE_REQUEST_NUMBER)
            .withModel(CAR_RESPONSE_REQUEST_MODEL)
            .withDeleted(true)
            .build();

    public static final DriverDto DRIVER_DTO = DriverDto.builder()
            .withId(DRIVER_ID)
            .withFirstName(DRIVER_RESPONSE_REQUEST_FIRST_NAME)
            .withLastName(DRIVER_RESPONSE_REQUEST_LAST_NAME)
            .withEmail(DRIVER_RESPONSE_REQUEST_EMAIL)
            .withPhone(DRIVER_RESPONSE_REQUEST_PHONE)
            .withGender(DRIVER_RESPONSE_REQUEST_GENDER)
            .withAverageRating(DRIVER_RESPONSE_REQUEST_AVERAGE_RATING)
            .withDeleted(true)
            .build();

    static {

        DRIVER_ENTITY = new Driver();
        DRIVER_ENTITY.setAverageRating(DRIVER_DTO.averageRating());
        DRIVER_ENTITY.setCars(new ArrayList<>());
        DRIVER_ENTITY.setDeleted(DRIVER_DTO.deleted());
        DRIVER_ENTITY.setEmail(DRIVER_DTO.email());
        DRIVER_ENTITY.setGender(DRIVER_DTO.gender());
        DRIVER_ENTITY.setId(DRIVER_DTO.id());
        DRIVER_ENTITY.setFirstName(DRIVER_DTO.firstName());
        DRIVER_ENTITY.setLastName(DRIVER_DTO.lastName());
        DRIVER_ENTITY.setPhone(DRIVER_DTO.phone());

        CAR_ENTITY = new Car();
        CAR_ENTITY.setBrand(CAR_DTO.brand());
        CAR_ENTITY.setColor(CAR_DTO.color());
        CAR_ENTITY.setDeleted(CAR_DTO.deleted());
        CAR_ENTITY.setDriver(DRIVER_ENTITY);
        CAR_ENTITY.setId(CAR_DTO.id());
        CAR_ENTITY.setModel(CAR_DTO.model());
        CAR_ENTITY.setNumber(CAR_DTO.number());
        CAR_ENTITY.setYear(CAR_DTO.year());

    }

}
