package com.modsen.ridesservice.service.component;

import com.modsen.ridesservice.client.driver.DriverFeignClient;
import com.modsen.ridesservice.client.driver.DriverResponseDto;
import com.modsen.ridesservice.client.passenger.PassengerFeignClient;
import com.modsen.ridesservice.client.passenger.PassengerResponseDto;
import com.modsen.ridesservice.dto.request.RideRequestDto;
import com.modsen.ridesservice.dto.request.RideStatusRequestDto;
import com.modsen.ridesservice.exception.ride.InvalidInputStatusException;
import com.modsen.ridesservice.model.Ride;
import com.modsen.ridesservice.model.enums.RideStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RideServiceValidationTest {

    @Mock
    private MessageSource messageSource;

    @Mock
    private PassengerFeignClient passengerFeignClient;

    @Mock
    private DriverFeignClient driverFeignClient;

    @InjectMocks
    private RideServiceValidation rideServiceValidation;

    @Test
    void testValidateChangingRideStatusFromCreatedToAccepted() {
        Ride ride = new Ride();
        ride.setRideStatus(RideStatus.CREATED);
        RideStatusRequestDto requestDto = new RideStatusRequestDto(RideStatus.ACCEPTED.name());

        rideServiceValidation.validateChangingRideStatus(ride, requestDto);
    }

    @Test
    void testValidateChangingRideStatusFromAcceptedToOnTheWayToPassenger() {
        Ride ride = new Ride();
        ride.setRideStatus(RideStatus.ACCEPTED);
        RideStatusRequestDto requestDto = new RideStatusRequestDto(RideStatus.ON_THE_WAY_TO_PASSENGER.name());

        rideServiceValidation.validateChangingRideStatus(ride, requestDto);
    }

    @Test
    void testValidateChangingRideStatusFromOnTheWayToPassengerToOnTheWayToDestination() {
        Ride ride = new Ride();
        ride.setRideStatus(RideStatus.ON_THE_WAY_TO_PASSENGER);
        RideStatusRequestDto requestDto = new RideStatusRequestDto(RideStatus.ON_THE_WAY_TO_DESTINATION.name());

        rideServiceValidation.validateChangingRideStatus(ride, requestDto);
    }

    @Test
    void testValidateChangingRideStatusFromOnTheWayToDestinationToCompleted() {
        Ride ride = new Ride();
        ride.setRideStatus(RideStatus.ON_THE_WAY_TO_DESTINATION);
        RideStatusRequestDto requestDto = new RideStatusRequestDto(RideStatus.COMPLETED.name());

        rideServiceValidation.validateChangingRideStatus(ride, requestDto);
    }

    @Test
    void testValidateChangingRideStatusInvalidTransition() {
        Ride ride = new Ride();
        ride.setRideStatus(RideStatus.CREATED);
        RideStatusRequestDto requestDto = new RideStatusRequestDto(RideStatus.ON_THE_WAY_TO_PASSENGER.name());

        assertThrows(InvalidInputStatusException.class, () -> {
            rideServiceValidation.validateChangingRideStatus(ride, requestDto);
        });
    }

    @Test
    void testValidateChangingRideStatusCanceled() {
        Ride ride = new Ride();
        ride.setRideStatus(RideStatus.CANCELED);
        RideStatusRequestDto requestDto = new RideStatusRequestDto(RideStatus.ACCEPTED.name());

        assertThrows(InvalidInputStatusException.class, () -> {
            rideServiceValidation.validateChangingRideStatus(ride, requestDto);
        });
    }

    @Test
    void testCheckExistingPassengerOrDriver() {
        RideRequestDto requestDto = new RideRequestDto(1L,
                1L,
                "Vilnius",
                "Riga");


        when(driverFeignClient.findDriverById(any(Long.class), any(String.class)))
                .thenReturn(new DriverResponseDto(1L,
                        "John",
                        "some.email@gmail.com",
                        "+123456789",
                        "MALE",
                        false));
        when(passengerFeignClient.findPassengerById(any(Long.class), any(String.class)))
                .thenReturn(new PassengerResponseDto(1L,
                        "John",
                        "Lock",
                        "some.email@gmail.com",
                        "+123456789",
                        false));

        rideServiceValidation.checkExistingPassengerOrDriver(requestDto);

        verify(driverFeignClient).findDriverById(any(Long.class), any(String.class));
        verify(passengerFeignClient).findPassengerById(any(Long.class), any(String.class));
    }

}