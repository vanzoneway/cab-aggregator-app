package com.modsen.driverservice;

import com.modsen.driverservice.dto.CarDto;
import com.modsen.driverservice.dto.DriverCarDto;
import com.modsen.driverservice.dto.DriverDto;
import com.modsen.driverservice.dto.ListContainerResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IntegrationTestData {

    public static final Long ID = 1L;
    private static final long ID_AFTER_CREATE = 2L;

    private static final String CAR_UPDATE_BRAND = "Toyota";
    private static final String CAR_UPDATE_COLOR = "Pink";
    private static final String CAR_UPDATE_NUMBER = "ABC123";
    private static final String CAR_UPDATE_MODEL = "Toyota Camry";
    private static final int CAR_UPDATE_YEAR = 2020;

    private static final String CAR_CREATE_BRAND = "BMW";
    private static final String CAR_CREATE_COLOR = "Black";
    private static final String CAR_CREATE_NUMBER = "ABC321";
    private static final String CAR_CREATE_MODEL = "E60";
    private static final int CAR_CREATE_YEAR = 2004;

    private static final String CAR_GET_BRAND = "Toyota";
    private static final String CAR_GET_COLOR = "Red";
    private static final String CAR_GET_NUMBER = "ABC123";
    private static final String CAR_GET_MODEL = "Toyota Camry";
    private static final int CAR_GET_YEAR = 2020;

    private static final String DRIVER_GET_FIRST_NAME = "John";
    private static final String DRIVER_GET_LAST_NAME = "Doe";
    private static final String DRIVER_GET_EMAIL = "johndoe@example.com";
    private static final String DRIVER_GET_PHONE = "+1234567890";
    private static final String DRIVER_GET_GENDER = "Male";

    private static final String DRIVER_CREATE_FIRST_NAME = "John";
    private static final String DRIVER_CREATE_LAST_NAME = "Lock";
    private static final String DRIVER_CREATE_EMAIL = "john.lock@gmail.com";
    private static final String DRIVER_CREATE_PHONE = "+1231231231";

    private static final String DRIVER_UPDATE_FIRST_NAME = "John";
    private static final String DRIVER_UPDATE_LAST_NAME = "Doe";
    private static final String DRIVER_UPDATE_EMAIL = "johndoe@example.com";
    private static final String DRIVER_UPDATE_PHONE = "+1234567899";

    private static final String TYPE_OF_SORT = "UNSORTED";

    public static final String SQL_DELETE_ALL_DATA = """
            DELETE FROM car;
            DELETE FROM driver;
            """;

    public static final String SQL_RESTART_SEQUENCES = """
            ALTER SEQUENCE car_id_seq RESTART WITH 1;
            ALTER SEQUENCE driver_id_seq RESTART WITH 1;
            """;

    public static final String SQL_INSERT_CAR_DRIVER = """
            INSERT INTO driver (first_name, last_name, email, phone, gender, deleted)
            VALUES ('John', 'Doe', 'johndoe@example.com', '+1234567890', 'Male', false);
   
            INSERT INTO car (brand, color, number, model, year, driver_id, deleted)
            VALUES ('Toyota', 'Red', 'ABC123', 'Toyota Camry', 2020, 1, false);
            """;

    public static final String BEARER = "Bearer ";

    public static final CarDto CAR_UPDATE_REQUEST_DTO = CarDto.builder()
            .withId(null)
            .withBrand(null)
            .withColor(CAR_UPDATE_COLOR)
            .withNumber(null)
            .withModel(null)
            .withYear(null)
            .withDeleted(null)
            .build();

    public static final CarDto CAR_UPDATE_RESPONSE_DTO = CarDto.builder()
            .withId(ID)
            .withBrand(CAR_UPDATE_BRAND)
            .withColor(CAR_UPDATE_COLOR)
            .withNumber(CAR_UPDATE_NUMBER)
            .withModel(CAR_UPDATE_MODEL)
            .withYear(CAR_UPDATE_YEAR)
            .withDeleted(false)
            .build();

    public static final CarDto CAR_CREATE_REQUEST_DTO = CarDto.builder()
            .withId(null)
            .withBrand(CAR_CREATE_BRAND)
            .withColor(CAR_CREATE_COLOR)
            .withNumber(CAR_CREATE_NUMBER)
            .withModel(CAR_CREATE_MODEL)
            .withYear(CAR_CREATE_YEAR)
            .withDeleted(null)
            .build();

    public static final CarDto CAR_CREATE_RESPONSE_DTO = CarDto.builder()
            .withId(ID_AFTER_CREATE)
            .withBrand(CAR_CREATE_BRAND)
            .withColor(CAR_CREATE_COLOR)
            .withNumber(CAR_CREATE_NUMBER)
            .withModel(CAR_CREATE_MODEL)
            .withYear(CAR_CREATE_YEAR)
            .withDeleted(false)
            .build();

    public static final CarDto CAR_GET_RESPONSE_DTO = CarDto.builder()
            .withId(ID)
            .withBrand(CAR_GET_BRAND)
            .withColor(CAR_GET_COLOR)
            .withNumber(CAR_GET_NUMBER)
            .withModel(CAR_GET_MODEL)
            .withYear(CAR_GET_YEAR)
            .withDeleted(false)
            .build();

    public static final DriverDto DRIVER_CREATE_REQUEST_DTO = DriverDto.builder()
            .withId(null)
            .withFirstName(DRIVER_CREATE_FIRST_NAME)
            .withLastName(DRIVER_CREATE_LAST_NAME)
            .withEmail(DRIVER_CREATE_EMAIL)
            .withPhone(DRIVER_CREATE_PHONE)
            .withGender(DRIVER_GET_GENDER)
            .withAverageRating(null)
            .withDeleted(null)
            .build();

    public static final DriverDto DRIVER_CREATE_RESPONSE_DTO = DriverDto.builder()
            .withId(ID_AFTER_CREATE)
            .withFirstName(DRIVER_CREATE_FIRST_NAME)
            .withLastName(DRIVER_CREATE_LAST_NAME)
            .withEmail(DRIVER_CREATE_EMAIL)
            .withPhone(DRIVER_CREATE_PHONE)
            .withGender(DRIVER_GET_GENDER)
            .withAverageRating(null)
            .withDeleted(false)
            .build();

    public static final DriverDto DRIVER_UPDATE_REQUEST_DTO = DriverDto.builder()
            .withId(null)
            .withFirstName(null)
            .withLastName(null)
            .withEmail(null)
            .withPhone(DRIVER_UPDATE_PHONE)
            .withGender(null)
            .withAverageRating(null)
            .withDeleted(null)
            .build();

    public static final DriverDto DRIVER_UPDATE_RESPONSE_DTO = DriverDto.builder()
            .withId(ID)
            .withFirstName(DRIVER_UPDATE_FIRST_NAME)
            .withLastName(DRIVER_UPDATE_LAST_NAME)
            .withEmail(DRIVER_UPDATE_EMAIL)
            .withPhone(DRIVER_UPDATE_PHONE)
            .withGender(DRIVER_GET_GENDER)
            .withAverageRating(null)
            .withDeleted(false)
            .build();

    public static final DriverDto DRIVER_GET_RESPONSE_DTO = DriverDto.builder()
            .withId(ID)
            .withFirstName(DRIVER_GET_FIRST_NAME)
            .withLastName(DRIVER_GET_LAST_NAME)
            .withEmail(DRIVER_GET_EMAIL)
            .withPhone(DRIVER_GET_PHONE)
            .withGender(DRIVER_GET_GENDER)
            .withAverageRating(null)
            .withDeleted(false)
            .build();

    public static final ListContainerResponseDto<DriverDto> DRIVER_PAGE_GET_RESPONSE_DTO =
            ListContainerResponseDto.<DriverDto>builder()
                    .withTotalPages(1)
                    .withCurrentLimit(10)
                    .withCurrentOffset(0)
                    .withSort(TYPE_OF_SORT)
                    .withTotalElements(1)
                    .withValues(List.of(DRIVER_GET_RESPONSE_DTO))
                    .build();

    public static final DriverCarDto DRIVER_CAR_RESPONSE_DTO = DriverCarDto.builder()
            .withId(DRIVER_GET_RESPONSE_DTO.id())
            .withFirstName(DRIVER_GET_RESPONSE_DTO.firstName())
            .withLastName(DRIVER_GET_RESPONSE_DTO.lastName())
            .withEmail(DRIVER_GET_RESPONSE_DTO.email())
            .withPhone(DRIVER_GET_RESPONSE_DTO.phone())
            .withGender(DRIVER_GET_RESPONSE_DTO.gender())
            .withAverageRating(DRIVER_GET_RESPONSE_DTO.averageRating())
            .withCars(List.of(CAR_GET_RESPONSE_DTO))
            .build();

}