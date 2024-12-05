package com.modsen.passengerservice.exception;

import com.modsen.passengerservice.constants.AppConstants;
import com.modsen.passengerservice.exception.passenger.DuplicatePassengerPhoneOrEmailException;
import com.modsen.passengerservice.exception.passenger.PassengerNotFoundException;
import com.modsen.passengerservice.exception.violation.ValidationErrorResponse;
import com.modsen.passengerservice.exception.violation.Violation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({PassengerNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiExceptionDto handleNotFoundException(Exception e) {
        return new ApiExceptionDto(HttpStatus.NOT_FOUND, e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler({DuplicatePassengerPhoneOrEmailException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiExceptionDto handleDuplicateException(Exception e) {
        return new ApiExceptionDto(HttpStatus.CONFLICT, e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler({AuthorizationDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiExceptionDto handleForbiddenException(AuthorizationDeniedException e) {
        return new ApiExceptionDto(HttpStatus.CONFLICT, e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiExceptionDto handleAnyException(Exception e) {
        log.error(e.getMessage(), e);
        return new ApiExceptionDto(
                HttpStatus.INTERNAL_SERVER_ERROR,
                AppConstants.INTERNAL_SERVER_ERROR,
                LocalDateTime.now());
    }

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
