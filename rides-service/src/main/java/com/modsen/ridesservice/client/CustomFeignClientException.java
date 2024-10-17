package com.modsen.ridesservice.client;

import com.modsen.ridesservice.exception.ApiExceptionDto;
import lombok.Getter;

@Getter
public class CustomFeignClientException extends RuntimeException {

    private final ApiExceptionDto apiExceptionDto;

    public CustomFeignClientException(ApiExceptionDto apiExceptionDto) {
        this.apiExceptionDto = apiExceptionDto;
    }

}
