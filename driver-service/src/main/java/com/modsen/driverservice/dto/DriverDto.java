package com.modsen.driverservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;

/**
 * DTO for {@link com.modsen.driverservice.model.Driver Driver}
 */
public record DriverDto(
        @NotNull(groups = Marker.OnGet.class) Long id,
        @NotBlank(message = "Name cannot be empty", groups = Marker.OnCreate.class) String name,
        @Email(message = "Invalid email form", groups = Marker.OnCreate.class) String email,
        @Pattern(message = "Invalid phone record format",
                regexp = "^\\+?[1-9][0-9]{7,14}$",
                groups = Marker.OnCreate.class) String phone,
        @Min(message = "Age cannot be less than 21", value = 21, groups = Marker.OnCreate.class) Integer age,
        @NotBlank(message = "Gender cannot be empty", groups = Marker.OnCreate.class) String gender,
        Boolean deleted
) implements Serializable {
}