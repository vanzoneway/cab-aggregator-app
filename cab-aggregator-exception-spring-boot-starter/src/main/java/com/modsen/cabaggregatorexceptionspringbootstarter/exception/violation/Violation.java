package com.modsen.cabaggregatorexceptionspringbootstarter.exception.violation;

public record Violation(

        String fieldName,

        String message) {
}
