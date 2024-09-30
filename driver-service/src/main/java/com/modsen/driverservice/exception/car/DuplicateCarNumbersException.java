package com.modsen.driverservice.exception.car;

public class DuplicateCarNumbersException extends RuntimeException {
    public DuplicateCarNumbersException(String message) {
        super(message);
    }
}
