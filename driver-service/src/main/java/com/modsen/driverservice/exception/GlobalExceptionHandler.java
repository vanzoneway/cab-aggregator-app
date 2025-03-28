package com.modsen.driverservice.exception;

import com.modsen.cabaggregatorexceptionspringbootstarter.exception.ApiExceptionDto;
import com.modsen.driverservice.exception.avatar.NoSuchAvatarException;
import com.modsen.driverservice.exception.avatar.UnsupportedFileTypeException;
import com.modsen.driverservice.exception.car.CarNotFoundException;
import com.modsen.driverservice.exception.car.DuplicateCarNumbersException;
import com.modsen.driverservice.exception.driver.DriverNotFoundException;
import com.modsen.driverservice.exception.driver.DuplicateDriverEmailPhoneException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import java.time.LocalDateTime;

@RestControllerAdvice
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

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    public ApiExceptionDto handleMaxUploadSizeExceededException(Exception e) {
        return new ApiExceptionDto(HttpStatus.PAYLOAD_TOO_LARGE, e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(UnsupportedFileTypeException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ApiExceptionDto handleUnsupportedFileTypeException(Exception e) {
        return new ApiExceptionDto(HttpStatus.UNSUPPORTED_MEDIA_TYPE, e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(NoSuchAvatarException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiExceptionDto handleNoSuchAvatarException(Exception e) {
        return new ApiExceptionDto(HttpStatus.NOT_FOUND, e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(MultipartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiExceptionDto handleMultipartException(Exception e) {
        return new ApiExceptionDto(HttpStatus.BAD_REQUEST, e.getMessage(), LocalDateTime.now());
    }

}
