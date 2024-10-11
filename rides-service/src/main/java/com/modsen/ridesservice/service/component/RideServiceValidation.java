package com.modsen.ridesservice.service.component;

import com.modsen.ridesservice.constants.AppConstants;
import com.modsen.ridesservice.dto.request.RideStatusRequestDto;
import com.modsen.ridesservice.exception.ride.InvalidInputStatusException;
import com.modsen.ridesservice.model.Ride;
import com.modsen.ridesservice.model.enums.RideStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RideServiceValidation {

    private final MessageSource messageSource;

    public void validateChangingRideStatus(Ride ride, RideStatusRequestDto rideStatusRequestDto) {
        RideStatus currentStatus = ride.getRideStatus();
        RideStatus newStatus = RideStatus.valueOf(rideStatusRequestDto.rideStatus());
        checkCanceledStatus(currentStatus);
        switch (currentStatus) {
            case CREATED:
                logicCheckStatus(currentStatus, newStatus, RideStatus.ACCEPTED);
                break;
            case ACCEPTED:
                logicCheckStatus(currentStatus, newStatus, RideStatus.ON_THE_WAY_TO_PASSENGER);
                break;
            case ON_THE_WAY_TO_PASSENGER:
                logicCheckStatus(currentStatus, newStatus, RideStatus.ON_THE_WAY_TO_DESTINATION);
                break;
            case ON_THE_WAY_TO_DESTINATION:
                logicCheckStatus(currentStatus, newStatus, RideStatus.COMPLETED);
                break;
        }
    }

    private void logicCheckStatus(RideStatus currentStatus, RideStatus newStatus, RideStatus potentialStatus) {
        if (newStatus != potentialStatus && newStatus != RideStatus.CANCELED) {
            throw new InvalidInputStatusException(messageSource.getMessage(
                    AppConstants.INVALID_INPUT_STATUS_MESSAGE_KEY,
                    new Object[]{currentStatus, newStatus},
                    LocaleContextHolder.getLocale()));
        }
    }

    private void checkCanceledStatus(RideStatus status) {
        if (status == RideStatus.CANCELED) {
            throw new InvalidInputStatusException(messageSource.getMessage(
                    AppConstants.CANCELED_STATUS_MESSAGE_KEY,
                    new Object[]{},
                    LocaleContextHolder.getLocale()
            ));
        }
    }

}