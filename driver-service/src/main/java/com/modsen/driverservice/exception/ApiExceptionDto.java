package com.modsen.driverservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ApiExceptionDto {

    HttpStatus status;

    String message;

    LocalDateTime timestamp;

}