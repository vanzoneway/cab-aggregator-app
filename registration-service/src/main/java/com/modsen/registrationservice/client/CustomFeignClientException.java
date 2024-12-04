package com.modsen.registrationservice.client;

import com.modsen.registrationservice.exception.ApiExceptionDto;
import lombok.Getter;

@Getter
public class CustomFeignClientException extends RuntimeException {

    private final ApiExceptionDto apiExceptionDto;

    public CustomFeignClientException(ApiExceptionDto apiExceptionDto) {
        this.apiExceptionDto = apiExceptionDto;
    }

}
