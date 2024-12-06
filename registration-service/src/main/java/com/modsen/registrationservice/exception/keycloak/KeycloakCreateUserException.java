package com.modsen.registrationservice.exception.keycloak;

import com.modsen.registrationservice.exception.ApiExceptionDto;
import lombok.Getter;

@Getter
public class KeycloakCreateUserException extends RuntimeException {

    private final ApiExceptionDto apiExceptionDto;

    public KeycloakCreateUserException(ApiExceptionDto apiExceptionDto) {
        this.apiExceptionDto = apiExceptionDto;
    }

}