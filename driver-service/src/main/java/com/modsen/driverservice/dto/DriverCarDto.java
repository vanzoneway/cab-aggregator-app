package com.modsen.driverservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Data Transfer Object for Driver with associated cars")
public class DriverCarDto {

    @Schema(description = "Unique identifier for the driver", example = "1")
    private Long id;

    private String firstName;

    private String lastName;

    @Schema(description = "Email of the driver", example = "johndoe@example.com")
    private String email;

    @Schema(description = "Phone number of the driver", example = "+1234567890")
    private String phone;

    @Schema(description = "Gender of the driver", example = "Male")
    private String gender;

    @Null(groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    Double averageRating;

    @Schema(description = "List of cars associated with the driver")
    private List<CarDto> cars;

}

