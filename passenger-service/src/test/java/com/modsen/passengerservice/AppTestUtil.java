package com.modsen.passengerservice;

import com.modsen.passengerservice.dto.ListContainerResponseDto;
import com.modsen.passengerservice.dto.PassengerDto;
import com.modsen.passengerservice.model.Passenger;

import java.util.ArrayList;

public final class AppTestUtil {

    private AppTestUtil() {
    }

    public static final String PASSENGER_ENDPOINT = "/api/v1/passengers";
    public static final String PASSENGER_UPDATE_DELETE_ENDPOINT = "/api/v1/passengers/{passengerId}";

    public static final PassengerDto PASSENGER_REQUEST_DTO = new PassengerDto(
            null,
            "Jane",
            "Doe",
            "jane.doe@example.org",
            "6625550144",
            null,
            null);

    public static final PassengerDto INVALID_PASSENGER_REQUEST_DTO = new PassengerDto(
            1L,
            "",
            "",
            "",
            "",
            5.0d,
            true);

    public static final PassengerDto PASSENGER_RESPONSE_DTO = new PassengerDto(
            1L,
            "Jane",
            "Doe",
            "jane.doe@example.org",
            "6625550144",
            5d,
            false);

    public static final ListContainerResponseDto<PassengerDto> PASSENGER_PAGE_RESPONSE_DTO =
            new ListContainerResponseDto<>(1,
                    1,
                    1,
                    1L,
                    "Sort",
                    new ArrayList<>());


    public static final Passenger PASSENGER;
    public static final PassengerDto PASSENGER_DTO;

    static {
        PASSENGER = new Passenger();
        PASSENGER.setId(1L);
        PASSENGER.setEmail("some.email@gmail.com");
        PASSENGER.setFirstName("John");
        PASSENGER.setLastName("Lock");
        PASSENGER.setAverageRating(5.0);
        PASSENGER.setPhone("+123456789");
        PASSENGER.setDeleted(false);
        PASSENGER_DTO = new PassengerDto(
                PASSENGER.getId(),
                PASSENGER.getFirstName(),
                PASSENGER.getLastName(),
                PASSENGER.getEmail(),
                PASSENGER.getPhone(),
                PASSENGER.getAverageRating(),
                PASSENGER.getDeleted());
    }

}
