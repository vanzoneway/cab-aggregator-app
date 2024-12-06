package com.modsen.registrationservice.client.driver;

import java.io.Serializable;

public record DriverResponseDto(
        Long id,
        String name,
        String email,
        String phone,
        String gender,
        Boolean deleted) implements Serializable {
}
