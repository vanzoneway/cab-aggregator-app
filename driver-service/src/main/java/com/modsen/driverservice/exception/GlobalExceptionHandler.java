package com.modsen.driverservice.exception;

import com.modsen.driverservice.aspect.AccessToResourcesDeniedException;
import com.modsen.driverservice.constants.AppConstants;
import com.modsen.driverservice.exception.car.CarNotFoundException;
import com.modsen.driverservice.exception.car.DuplicateCarNumbersException;
import com.modsen.driverservice.exception.driver.DriverNotFoundException;
import com.modsen.driverservice.exception.driver.DuplicateDriverEmailPhoneException;
import com.modsen.driverservice.exception.violation.ValidationErrorResponse;
import com.modsen.driverservice.exception.violation.Violation;
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

    @ExceptionHandler({DriverNotFoundException.class, CarNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiExceptionDto handleDriverNotFoundException(Exception e) {
        return new ApiExceptionDto(HttpStatus.NOT_FOUND, e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler({DuplicateCarNumbersException.class, DuplicateDriverEmailPhoneException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiExceptionDto handleDuplicateCarNumbersException(Exception e) {
        return new ApiExceptionDto(HttpStatus.CONFLICT, e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler({AccessToResourcesDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiExceptionDto handleAccessToResourcesDeniedException(AccessToResourcesDeniedException e) {
        return new ApiExceptionDto(HttpStatus.FORBIDDEN, e.getMessage(), LocalDateTime.now());
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
                                violation.getMessage()
                        )
                )
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
