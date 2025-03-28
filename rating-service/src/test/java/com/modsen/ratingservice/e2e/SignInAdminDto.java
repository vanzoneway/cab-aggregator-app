package com.modsen.ratingservice.e2e;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record SignInAdminDto(

        @NotBlank
        String grantType,

        @NotBlank
        String clientId,

        @NotBlank
        String clientSecret) implements Serializable {
}
