package com.modsen.driverservice.aspect;

public class AccessToResourcesDeniedException extends RuntimeException {

    public AccessToResourcesDeniedException(String message) {
        super(message);
    }

}
