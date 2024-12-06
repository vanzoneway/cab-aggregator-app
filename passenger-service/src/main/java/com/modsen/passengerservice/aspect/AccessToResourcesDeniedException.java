package com.modsen.passengerservice.aspect;

public class AccessToResourcesDeniedException extends RuntimeException {
    public AccessToResourcesDeniedException(String message) {
        super(message);
    }
}
