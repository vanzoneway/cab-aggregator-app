package com.modsen.passengerservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;

/**
 * DTO for {@link com.modsen.passengerservice.model.Passenger}
 */
@Schema(description = "Data Transfer Object for Passenger")
public record PassengerDto(
        @NotNull(groups = Marker.OnGet.class)
        @Null(groups = {Marker.OnCreate.class, Marker.OnUpdate.class}, message = "{id.must.be.empty}")
        @Schema(hidden = true)
        Long id,

        @NotBlank(message = "{firstname.empty}", groups = Marker.OnCreate.class)
        @Schema(description = "First name of the passenger", example = "Jane")
        String firstName,

        @NotBlank(message = "{lastname.empty}", groups = Marker.OnCreate.class)
        @Schema(description = "Last name of the passenger", example = "Doe")
        String lastName,

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

        @Null(groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
        Double averageRating,

        @Null(groups = {Marker.OnCreate.class, Marker.OnUpdate.class}, message = "{deleted.must.be.null}")
        @Schema(hidden = true)
        Boolean deleted) implements Serializable {
}