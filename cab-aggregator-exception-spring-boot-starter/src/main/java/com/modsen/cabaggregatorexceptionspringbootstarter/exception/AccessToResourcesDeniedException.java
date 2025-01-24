package com.modsen.cabaggregatorexceptionspringbootstarter.exception;

public class AccessToResourcesDeniedException extends RuntimeException {

    public AccessToResourcesDeniedException(String message) {
        super(message);
    }

}
