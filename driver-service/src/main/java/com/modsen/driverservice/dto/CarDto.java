package com.modsen.driverservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;

/**
 * DTO for {@link com.modsen.driverservice.model.Car Car}
 */
public record CarDto(
        @NotNull(groups = Marker.OnGet.class) Long id,
        @NotBlank(message = "Brand cannot be empty", groups = Marker.OnCreate.class) String brand,
        @NotBlank(message = "Color cannot be empty", groups = Marker.OnCreate.class) String color,
        @Pattern(message = "Invalid car number",
                regexp = "^[A-Z]{1,2}[0-9]{3}[A-Z]{2,3}$|^[A-Z]{1,2}-[0-9]{3}-[A-Z]" +
                        "{2,3}$|^[0-9]{3}-[A-Z]{1,2}-[0-9]{2}$|^[A-Z]{2}-[0-9]{3}-[A-Z]{1,2}$",
                groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
        @NotBlank(groups = Marker.OnCreate.class) String number,
        @NotBlank(message = "Model cannot be empty", groups = Marker.OnCreate.class) String model,
        @NotNull(message = "Year cannot be empty", groups = Marker.OnCreate.class) Integer year,
        Boolean deleted
) implements Serializable {
}