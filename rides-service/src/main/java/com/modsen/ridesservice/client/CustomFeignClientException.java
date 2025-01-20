package com.modsen.ridesservice.client;

import com.modsen.cabaggregatorspringbootstarter.exception.ApiExceptionDto;
import lombok.Getter;

@Getter
public class CustomFeignClientException extends RuntimeException {

    private final ApiExceptionDto apiExceptionDto;

    public CustomFeignClientException(ApiExceptionDto apiExceptionDto) {
        this.apiExceptionDto = apiExceptionDto;
    }

}
