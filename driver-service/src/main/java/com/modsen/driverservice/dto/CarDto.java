package com.modsen.driverservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;

/**
 * DTO for {@link com.modsen.driverservice.model.Car Car}
 */
@Schema(description = "Data Transfer Object for Car")
public record CarDto(
        @NotNull(groups = Marker.OnGet.class)
        @Schema(description = "Unique identifier for the car", example = "1")
        Long id,

        @NotBlank(message = "{brand.empty}", groups = Marker.OnCreate.class)
        @Schema(description = "Brand of the car", example = "Toyota")
        String brand,

        @NotBlank(message = "{color.empty}", groups = Marker.OnCreate.class)
        @Schema(description = "Color of the car", example = "Red")
        String color,

        @Pattern(message = "{number.invalid}",
                regexp = "^[A-Z]{3}[0-9]{3}$",
                groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
        @NotBlank(groups = Marker.OnCreate.class, message = "{number.empty}")
        @Schema(description = "Car registration number", example = "AB123CD")
        String number,

        @NotBlank(message = "{model.empty}", groups = Marker.OnCreate.class)
        @Schema(description = "Model of the car", example = "Camry")
        String model,

        @NotNull(message = "{year.empty}", groups = Marker.OnCreate.class)
        @Schema(description = "Year the car was manufactured", example = "2020")
        Integer year,

        @Schema(hidden = true)
        Boolean deleted) implements Serializable {
}
