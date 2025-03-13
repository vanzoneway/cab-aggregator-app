package com.modsen.driverservice.dto;

import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class DriverCarDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String gender;

    @Null(groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    Double averageRating;

    private List<CarDto> cars;

}

