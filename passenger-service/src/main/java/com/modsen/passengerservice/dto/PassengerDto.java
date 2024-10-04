package com.modsen.passengerservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;

/**
 * DTO for {@link com.modsen.passengerservice.model.Passenger}
 */
@Schema(description = "Data Transfer Object for Passenger")
public record PassengerDto(
        @NotNull(groups = Marker.OnGet.class)
        @Schema(description = "Unique identifier for the passenger", example = "1")
        Long id,

        @NotBlank(message = "{name.empty}", groups = Marker.OnCreate.class)
        @Schema(description = "Name of the passenger", example = "Jane Doe")
        String name,

        @Email(message = "{email.invalid}", groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
        @Schema(description = "Email address of the passenger", example = "janedoe@example.com")
        @NotBlank(message = "{email.empty}", groups = Marker.OnCreate.class)
        String email,

        @Pattern(message = "{phone.invalid}",
                regexp = "^\\+?[1-9][0-9]{7,14}$",
                groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
        @Schema(description = "Phone number of the passenger", example = "+1234567890")
        @NotBlank(message = "{phone.empty}", groups = Marker.OnCreate.class)
        String phone,

        Boolean deleted) implements Serializable {
}