package com.modsen.passengerservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;

public record PassengerDto(
        @NotNull(groups = Marker.OnGet.class)
        @Null(groups = {Marker.OnCreate.class, Marker.OnUpdate.class}, message = "{id.must.be.empty}")
        Long id,

        @NotBlank(message = "{firstname.empty}", groups = Marker.OnCreate.class)
        @Null(groups = Marker.OnUpdate.class)
        String firstName,

        @NotBlank(message = "{lastname.empty}", groups = Marker.OnCreate.class)
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

        @Null(groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
        Double averageRating,

        @Null(groups = {Marker.OnCreate.class, Marker.OnUpdate.class}, message = "{deleted.must.be.null}")
        Boolean deleted) implements Serializable {
}