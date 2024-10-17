package com.modsen.ridesservice.client.driver;

import java.io.Serializable;

public record DriverResponseDto(
        Long id,
        String name,
        String email,
        String phone,
        String gender,
        Boolean deleted) implements Serializable {
}
