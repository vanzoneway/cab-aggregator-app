package com.modsen.passengerservice.exception;

import com.modsen.cabaggregatorexceptionspringbootstarter.exception.ApiExceptionDto;
import com.modsen.passengerservice.exception.avatar.NoSuchAvatarException;
import com.modsen.passengerservice.exception.avatar.UnsupportedFileTypeException;
import com.modsen.passengerservice.exception.passenger.DuplicatePassengerPhoneOrEmailException;
import com.modsen.passengerservice.exception.passenger.PassengerNotFoundException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import java.time.LocalDateTime;

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
