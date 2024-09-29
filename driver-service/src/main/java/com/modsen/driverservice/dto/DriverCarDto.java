package com.modsen.driverservice.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DriverCarDto {

    Long id;
    String name;
    String email;
    String phone;
    Integer age;
    String gender;
    List<CarDto> cars;

}
