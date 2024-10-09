package com.modsen.ratingservice.exception;

import com.modsen.ratingservice.constants.AppConstants;
import com.modsen.ratingservice.exception.rating.DriverRatingNotFoundException;
import com.modsen.ratingservice.exception.rating.DuplicateRideIdException;
import com.modsen.ratingservice.exception.rating.PassengerRatingNotFoundException;
import com.modsen.ratingservice.exception.violation.ValidationErrorResponse;
import com.modsen.ratingservice.exception.violation.Violation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({DriverRatingNotFoundException.class, PassengerRatingNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiExceptionDto handleNotFoundException(Exception e) {
        return new ApiExceptionDto(HttpStatus.NOT_FOUND, e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler({DuplicateRideIdException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiExceptionDto handleDuplicateException(Exception e) {
        return new ApiExceptionDto(HttpStatus.CONFLICT, e.getMessage(), LocalDateTime.now());
    }

//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ApiExceptionDto handleAnyException(Exception e) {
//        return new ApiExceptionDto(
//                HttpStatus.INTERNAL_SERVER_ERROR,
//                AppConstants.INTERNAL_SERVER_ERROR,
//                LocalDateTime.now());
//    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleConstraintValidationException(ConstraintViolationException e) {
        final List<Violation> violations = e.getConstraintViolations().stream()
                .map(
                        violation -> new Violation(
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
