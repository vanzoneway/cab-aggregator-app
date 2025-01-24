package com.modsen.ratingservice.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.modsen.cabaggregatorexceptionspringbootstarter.exception.ApiExceptionDto;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component
public class RideFeignClientErrorDecoder implements ErrorDecoder {

    @Override
    @SneakyThrows
    public Exception decode(String s, Response response) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ApiExceptionDto apiExceptionDto;
        apiExceptionDto = objectMapper.readValue(readResponseBody(response), ApiExceptionDto.class);
        return new RideFeignClientException(apiExceptionDto);
    }

    @SneakyThrows
    private String readResponseBody(Response response) {
        if (Objects.nonNull(response.body())) {
            StringBuilder builder = new StringBuilder();
            @Cleanup InputStreamReader inputStreamReader = new InputStreamReader(response.body()
                    .asInputStream(), StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        }
        return "";
    }

}
