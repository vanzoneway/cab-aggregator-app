package com.modsen.registrationservice.exception;

import com.modsen.cabaggregatorspringbootstarter.exception.ApiExceptionDto;
import com.modsen.registrationservice.client.CustomFeignClientException;
import com.modsen.registrationservice.exception.keycloak.KeycloakCreateUserException;
import com.modsen.registrationservice.exception.keycloak.KeycloakException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.ws.rs.ServiceUnavailableException;
import java.time.LocalDateTime;

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


    @ExceptionHandler(CustomFeignClientException.class)
    public ResponseEntity<ApiExceptionDto> handleRideNotFoundException(CustomFeignClientException e) {
        ApiExceptionDto exceptionDto = e.getApiExceptionDto();
        return ResponseEntity.status(exceptionDto.getStatus()).body(e.getApiExceptionDto());
    }

    @ExceptionHandler(KeycloakCreateUserException.class)
    public ResponseEntity<ApiExceptionDto> handleKeycloakCreateUserException(KeycloakCreateUserException e) {
        ApiExceptionDto exceptionDto = e.getApiExceptionDto();
        return ResponseEntity.status(exceptionDto.getStatus()).body(e.getApiExceptionDto());
    }

    @ExceptionHandler(KeycloakException.class)
    public ResponseEntity<ApiExceptionDto> handleKeycloakException(KeycloakException e) {
        ApiExceptionDto exceptionDto = e.getApiExceptionDto();
        return ResponseEntity.status(exceptionDto.getStatus()).body(e.getApiExceptionDto());
    }


}
