package com.modsen.driverservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;

/**
 * DTO for {@link com.modsen.driverservice.model.Car Car}
 */
public record CarDto(
        @NotBlank(message = "Brand cannot be empty") String brand,
        @NotBlank(message = "Color cannot be empty") String color,
        @Pattern(message = "Invalid car number",
                regexp = "^[A-Z]{1,2}[0-9]{3}[A-Z]{2,3}$|^[A-Z]{1,2}-[0-9]{3}-[A-Z]" +
                        "{2,3}$|^[0-9]{3}-[A-Z]{1,2}-[0-9]{2}$|^[A-Z]{2}-[0-9]{3}-[A-Z]{1,2}$") @NotBlank String number,
        @NotBlank(message = "Model cannot be empty") String model,
        Integer year
) implements Serializable {
}