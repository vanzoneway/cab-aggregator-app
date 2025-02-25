package com.modsen.driverservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.io.Serializable;

/**
 * DTO for {@link com.modsen.driverservice.model.Driver Driver}
 */
@Schema(description = "Data Transfer Object for Driver")
@Builder(setterPrefix = "with")
public record DriverDto(
        @NotNull(groups = Marker.OnGet.class)
        @Schema(description = "Unique identifier for the driver", example = "1")
        Long id,

        @NotBlank(message = "{firstname.empty}", groups = Marker.OnCreate.class)
        @Null(groups = Marker.OnUpdate.class)
        String firstName,

        @NotBlank(message = "{lastname.empty}", groups = Marker.OnCreate.class)
        @Null(groups = Marker.OnUpdate.class)
        String lastName,

        @Email(message = "{email.invalid}", groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
        @Schema(description = "Email address of the driver", example = "janedoe@example.com")
        @NotBlank(message = "{email.empty}", groups = Marker.OnCreate.class)
        @Null(groups = Marker.OnUpdate.class)
        String email,

        @Pattern(message = "{phone.invalid}",
                regexp = "^\\+?[1-9][0-9]{7,14}$",
                groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
        @Schema(description = "Phone number of the driver", example = "+1234567890")
        @NotBlank(message = "{phone.empty}", groups = Marker.OnCreate.class)
        String phone,

        @NotBlank(message = "{gender.empty}", groups = Marker.OnCreate.class)
        @Schema(description = "Gender of the driver", example = "Female")
        @Null(groups = Marker.OnUpdate.class)
        String gender,

        @Null(groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
        Double averageRating,

        @Schema(hidden = true)
        Boolean deleted) implements Serializable {
}