package com.modsen.passengerservice.exception.passenger;

public class DuplicatePassengerPhoneOrEmailException extends RuntimeException {
    public DuplicatePassengerPhoneOrEmailException(String message) {
        super(message);
    }
}
