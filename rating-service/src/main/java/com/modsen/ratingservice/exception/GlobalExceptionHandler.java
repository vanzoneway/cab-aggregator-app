package com.modsen.ratingservice.exception;

import com.modsen.cabaggregatorexceptionspringbootstarter.exception.ApiExceptionDto;
import com.modsen.ratingservice.client.RideFeignClientException;
import com.modsen.ratingservice.exception.rating.RatingNotFoundException;
import com.modsen.ratingservice.exception.rating.DuplicateRideIdException;
import com.modsen.ratingservice.exception.rating.RefUserIdNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({RatingNotFoundException.class, RefUserIdNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiExceptionDto handleNotFoundException(Exception e) {
        return new ApiExceptionDto(HttpStatus.NOT_FOUND, e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(DuplicateRideIdException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiExceptionDto handleDuplicateException(Exception e) {
        return new ApiExceptionDto(HttpStatus.CONFLICT, e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(RideFeignClientException.class)
    public ResponseEntity<ApiExceptionDto> handleRideNotFoundException(RideFeignClientException e) {
        ApiExceptionDto exceptionDto = e.getApiExceptionDto();
        return ResponseEntity.status(exceptionDto.getStatus()).body(e.getApiExceptionDto());
    }

}
