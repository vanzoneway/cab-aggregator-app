package com.modsen.cabaggregatorspringbootstarter.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Data Transfer Object for API exceptions")
public class ApiExceptionDto  implements Serializable {

    @Schema(description = "HTTP status of the error", example = "404", required = true)
    private HttpStatus status;

    @Schema(description = "Error message describing the issue", example = "Resource not found", required = true)
    private String message;

    @Schema(description = "Timestamp when the error occurred", example = "2023-09-30T15:30:00")
    private LocalDateTime timestamp;

}