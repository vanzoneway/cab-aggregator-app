package com.modsen.driverservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.io.Serializable;

@Builder(setterPrefix = "with")
public record CarDto(
        @NotNull(groups = Marker.OnGet.class)
        Long id,

        @NotBlank(message = "{brand.empty}", groups = Marker.OnCreate.class)
        String brand,

        @NotBlank(message = "{color.empty}", groups = Marker.OnCreate.class)
        String color,

        @Pattern(message = "{number.invalid}",
                regexp = "^[A-Z]{3}[0-9]{3}$",
                groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
        @NotBlank(groups = Marker.OnCreate.class, message = "{number.empty}")
        String number,

        @NotBlank(message = "{model.empty}", groups = Marker.OnCreate.class)
        String model,

        @NotNull(message = "{year.empty}", groups = Marker.OnCreate.class)
        Integer year,

        Boolean deleted) implements Serializable {
}
