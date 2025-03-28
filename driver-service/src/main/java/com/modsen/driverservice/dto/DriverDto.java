package com.modsen.driverservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.io.Serializable;

@Builder(setterPrefix = "with")
public record DriverDto(
        @NotNull(groups = Marker.OnGet.class)
        Long id,

        @NotBlank(message = "{firstname.empty}", groups = Marker.OnCreate.class)
        @Null(groups = Marker.OnUpdate.class)
        String firstName,

        @NotBlank(message = "{lastname.empty}", groups = Marker.OnCreate.class)
        @Null(groups = Marker.OnUpdate.class)
        String lastName,

        @Email(message = "{email.invalid}", groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
        @NotBlank(message = "{email.empty}", groups = Marker.OnCreate.class)
        @Null(groups = Marker.OnUpdate.class)
        String email,

        @Pattern(message = "{phone.invalid}",
                regexp = "^\\+?[1-9][0-9]{7,14}$",
                groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
        @NotBlank(message = "{phone.empty}", groups = Marker.OnCreate.class)
        String phone,

        @NotBlank(message = "{gender.empty}", groups = Marker.OnCreate.class)
        @Null(groups = Marker.OnUpdate.class)
        String gender,

        @Null(groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
        Double averageRating,

        Boolean deleted) implements Serializable {
}