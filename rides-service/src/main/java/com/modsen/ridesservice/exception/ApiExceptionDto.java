package com.modsen.ridesservice.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

@Schema(description = "Data Transfer Object for API exceptions")
public record ApiExceptionDto(
        @Schema(description = "HTTP status of the error", example = "404")
        HttpStatus status,

        @Schema(description = "Error message describing the issue", example = "Resource not found")
        String message,

        @Schema(description = "Timestamp when the error occurred", example = "2023-09-30T15:30:00")
        LocalDateTime timestamp) implements Serializable {
}