package com.modsen.ridesservice.exception;

import com.modsen.ridesservice.constants.AppConstants;
import com.modsen.ridesservice.exception.ride.RideNotFoundException;
import com.modsen.ridesservice.exception.violation.ValidationErrorResponse;
import com.modsen.ridesservice.exception.violation.Violation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RideNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiExceptionDto handleRideNotFoundException(RideNotFoundException e) {
        return new ApiExceptionDto(
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiExceptionDto handleAnyException(Exception e) {
        return new ApiExceptionDto(
                HttpStatus.INTERNAL_SERVER_ERROR,
                AppConstants.INTERNAL_SERVER_ERROR,
                LocalDateTime.now());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiExceptionDto handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return new ApiExceptionDto(
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleConstraintValidationException(ConstraintViolationException e) {
        final List<Violation> violations = e.getConstraintViolations().stream()
                .map(violation -> new Violation(
                        violation.getPropertyPath().toString().replaceFirst(".*\\.", ""),
                        violation.getMessage()))
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }

}