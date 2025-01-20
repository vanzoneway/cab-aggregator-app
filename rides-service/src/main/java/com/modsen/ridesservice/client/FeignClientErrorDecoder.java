package com.modsen.ridesservice.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.modsen.cabaggregatorspringbootstarter.exception.ApiExceptionDto;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component
public class FeignClientErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder.Default eDefault = new ErrorDecoder.Default();

    @Override
    @SneakyThrows
    public Exception decode(String s, Response response) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ApiExceptionDto apiExceptionDto;
        if (response.status() >= HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            return eDefault.decode(s, response);
        }
        apiExceptionDto = objectMapper.readValue(readResponseBody(response), ApiExceptionDto.class);
        return new CustomFeignClientException(apiExceptionDto);
    }

    @SneakyThrows
    private String readResponseBody(Response response) {
        if (Objects.nonNull(response.body())) {
            @Cleanup InputStreamReader inputStreamReader = new InputStreamReader(response.body()
                    .asInputStream(), StandardCharsets.UTF_8);
            StringBuilder builder = new StringBuilder();
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
