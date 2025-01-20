package com.modsen.cabaggregatorspringbootstarter.exception;

public class AccessToResourcesDeniedException extends RuntimeException {

    public AccessToResourcesDeniedException(String message) {
        super(message);
    }

}
