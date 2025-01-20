package com.modsen.registrationservice.exception.keycloak;

import com.modsen.cabaggregatorspringbootstarter.exception.ApiExceptionDto;
import lombok.Getter;

@Getter
public class KeycloakException extends RuntimeException {

    private final ApiExceptionDto apiExceptionDto;

    public KeycloakException(ApiExceptionDto apiExceptionDto) {
        this.apiExceptionDto = apiExceptionDto;
    }

}