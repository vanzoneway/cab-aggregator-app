package com.modsen.ratingservice.exception.rating;

public class DuplicateRideIdException extends RuntimeException {
    public DuplicateRideIdException(String message) {
        super(message);
    }
}
