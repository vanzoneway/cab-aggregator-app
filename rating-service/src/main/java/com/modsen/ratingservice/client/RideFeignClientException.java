package com.modsen.ratingservice.client;

import com.modsen.cabaggregatorspringbootstarter.exception.ApiExceptionDto;
import lombok.Getter;

@Getter
public class RideFeignClientException extends RuntimeException {

    private final ApiExceptionDto apiExceptionDto;

    public RideFeignClientException(ApiExceptionDto apiExceptionDto) {
        this.apiExceptionDto = apiExceptionDto;
    }

}
