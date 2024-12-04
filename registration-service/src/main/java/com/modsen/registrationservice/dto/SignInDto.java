package com.modsen.registrationservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record SignInDto(
        @Email(message = "{email.invalid}")
        @NotBlank(message = "{email.empty}")
        String email,

        @NotBlank(message = "{empty.password}")
        String password) implements Serializable {
}
