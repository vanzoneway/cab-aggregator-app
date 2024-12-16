package com.modsen.registrationservice.exception;

import com.modsen.registrationservice.client.CustomFeignClientException;
import com.modsen.registrationservice.constants.AppConstants;
import com.modsen.registrationservice.exception.keycloak.KeycloakCreateUserException;
import com.modsen.registrationservice.exception.keycloak.KeycloakException;
import com.modsen.registrationservice.exception.violation.ValidationErrorResponse;
import com.modsen.registrationservice.exception.violation.Violation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.ws.rs.ServiceUnavailableException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceUnavailableException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ApiExceptionDto handleServiceUnavailableException(Exception e) {
        log.error(e.getMessage(), e);
        return new ApiExceptionDto(
                HttpStatus.SERVICE_UNAVAILABLE,
                e.getMessage(),
                LocalDateTime.now());
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

    @ExceptionHandler(CustomFeignClientException.class)
    public ResponseEntity<ApiExceptionDto> handleRideNotFoundException(CustomFeignClientException e) {
        ApiExceptionDto exceptionDto = e.getApiExceptionDto();
        return ResponseEntity.status(exceptionDto.status()).body(e.getApiExceptionDto());
    }

    @ExceptionHandler(KeycloakCreateUserException.class)
    public ResponseEntity<ApiExceptionDto> handleKeycloakCreateUserException(KeycloakCreateUserException e) {
        ApiExceptionDto exceptionDto = e.getApiExceptionDto();
        return ResponseEntity.status(exceptionDto.status()).body(e.getApiExceptionDto());
    }

    @ExceptionHandler(KeycloakException.class)
    public ResponseEntity<ApiExceptionDto> handleKeycloakException(KeycloakException e) {
        ApiExceptionDto exceptionDto = e.getApiExceptionDto();
        return ResponseEntity.status(exceptionDto.status()).body(e.getApiExceptionDto());
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
