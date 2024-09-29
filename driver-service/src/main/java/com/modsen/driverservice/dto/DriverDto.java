package com.modsen.driverservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;

/**
 * DTO for {@link com.modsen.driverservice.model.Driver Driver}
 */
public record DriverDto(
        @NotBlank(message = "Name cannot be empty") String name,
        @Email(message = "Invalid email form") String email,
        @Pattern(message = "Invalid phone record format", regexp = "\"^\\\\+?[0-9]{10,15}$\"") String phone,
        @Min(message = "Age cannot be less than 21", value = 21) Integer age,
        @NotBlank(message = "Gender cannot be empty") String gender
) implements Serializable {
}