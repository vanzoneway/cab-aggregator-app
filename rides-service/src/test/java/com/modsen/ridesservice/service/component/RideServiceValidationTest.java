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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RideServiceValidationTest {

    @Spy
    MessageSource messageSource;

    @Mock
    private PassengerFeignClient passengerFeignClient;

    @Mock
    private DriverFeignClient driverFeignClient;

    @InjectMocks
    private RideServiceValidation rideServiceValidation;

    @Test
    void validateChangingRideStatus_ChangesRideStatus_FromCreatedToAccepted() {
        Ride ride = new Ride();
        ride.setRideStatus(RideStatus.CREATED);
        RideStatusRequestDto requestDto = new RideStatusRequestDto(RideStatus.ACCEPTED.name());

        assertThatCode(() -> rideServiceValidation.validateChangingRideStatus(ride, requestDto))
                .doesNotThrowAnyException();
    }

    @Test
    void validateChangingRideStatus_ChangesRideStatus_FromAcceptedToOnTheWayToPassenger() {
        Ride ride = new Ride();
        ride.setRideStatus(RideStatus.ACCEPTED);
        RideStatusRequestDto requestDto = new RideStatusRequestDto(RideStatus.ON_THE_WAY_TO_PASSENGER.name());

        assertThatCode(() -> rideServiceValidation.validateChangingRideStatus(ride, requestDto))
                .doesNotThrowAnyException();
    }

    @Test
    void validateChangingRideStatus_ChangesRideStatus_FromOnTheWayToPassengerToOnTheWayToDestination_Success() {
        Ride ride = new Ride();
        ride.setRideStatus(RideStatus.ON_THE_WAY_TO_PASSENGER);
        RideStatusRequestDto requestDto = new RideStatusRequestDto(RideStatus.ON_THE_WAY_TO_DESTINATION.name());

        assertThatCode(() -> rideServiceValidation.validateChangingRideStatus(ride, requestDto))
                .doesNotThrowAnyException();
    }

    @Test
    void validateChangingRideStatus_ChangesRideStatus_FromOnTheWayToDestinationToCompleted() {
        Ride ride = new Ride();
        ride.setRideStatus(RideStatus.ON_THE_WAY_TO_DESTINATION);
        RideStatusRequestDto requestDto = new RideStatusRequestDto(RideStatus.COMPLETED.name());

        assertThatCode(() -> rideServiceValidation.validateChangingRideStatus(ride, requestDto))
                .doesNotThrowAnyException();
    }

    @Test
    void validateChangingRideStatus_ChangesRideStatus_InvalidTransition() {
        Ride ride = new Ride();
        ride.setRideStatus(RideStatus.CREATED);
        RideStatusRequestDto requestDto = new RideStatusRequestDto(RideStatus.ON_THE_WAY_TO_PASSENGER.name());

        assertThatThrownBy(() -> rideServiceValidation.validateChangingRideStatus(ride, requestDto))
                .isInstanceOf(InvalidInputStatusException.class);
    }

    @Test
    void validateChangingRideStatus_ChangesRideStatus_Canceled() {
        Ride ride = new Ride();
        ride.setRideStatus(RideStatus.CANCELED);
        RideStatusRequestDto requestDto = new RideStatusRequestDto(RideStatus.ACCEPTED.name());

        assertThatThrownBy(() ->
            rideServiceValidation.validateChangingRideStatus(ride, requestDto))
                .isInstanceOf(InvalidInputStatusException.class);
    }

    @Test
    void checkExistingPassengerOrDriver_AllMethodsAreCalled_ValidInputArguments() {
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