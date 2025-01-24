package com.modsen.ridesservice.exception;

import com.modsen.cabaggregatorexceptionspringbootstarter.exception.ApiExceptionDto;
import com.modsen.ridesservice.client.CustomFeignClientException;
import com.modsen.ridesservice.exception.ride.InvalidInputStatusException;
import com.modsen.ridesservice.exception.ride.RideNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({RideNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiExceptionDto handleRideNotFoundException(Exception e) {
        return new ApiExceptionDto(
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiExceptionDto handleHttpMessageNotReadableException(Exception e) {
        return new ApiExceptionDto(
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler({InvalidInputStatusException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiExceptionDto handleInvalidInputStatusException(Exception e) {
        return new ApiExceptionDto(
                HttpStatus.CONFLICT,
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler(CustomFeignClientException.class)
    public ResponseEntity<ApiExceptionDto> handleRideNotFoundException(CustomFeignClientException e) {
        ApiExceptionDto exceptionDto = e.getApiExceptionDto();
        return ResponseEntity.status(exceptionDto.getStatus()).body(e.getApiExceptionDto());
    }

}
