package com.modsen.driverservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;

/**
 * DTO for {@link com.modsen.driverservice.model.Driver Driver}
 */
@Schema(description = "Data Transfer Object for Driver")
public record DriverDto(
        @NotNull(groups = Marker.OnGet.class)
        @Schema(description = "Unique identifier for the driver", example = "1")
        Long id,

        @NotBlank(message = "Name cannot be empty", groups = Marker.OnCreate.class)
        @Schema(description = "Name of the driver", example = "Jane Doe")
        String name,

        @Email(message = "Invalid email form", groups = Marker.OnCreate.class)
        @Schema(description = "Email address of the driver", example = "janedoe@example.com")
        String email,

        @Pattern(message = "Invalid phone record format",
                regexp = "^\\+?[1-9][0-9]{7,14}$",
                groups = Marker.OnCreate.class)
        @Schema(description = "Phone number of the driver", example = "+1234567890")
        String phone,

        @Min(message = "Age cannot be less than 21", value = 21, groups = Marker.OnCreate.class)
        @Schema(description = "Age of the driver", example = "30")
        Integer age,

        @NotBlank(message = "Gender cannot be empty", groups = Marker.OnCreate.class)
        @Schema(description = "Gender of the driver", example = "Female")
        String gender,

        @Schema(hidden = true)
        Boolean deleted
) implements Serializable {
}