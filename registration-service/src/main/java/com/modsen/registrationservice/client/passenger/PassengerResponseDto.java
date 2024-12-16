package com.modsen.registrationservice.client.passenger;

import java.io.Serializable;

public record PassengerResponseDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        Boolean deleted) implements Serializable {
}
