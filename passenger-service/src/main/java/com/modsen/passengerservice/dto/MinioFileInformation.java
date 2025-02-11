package com.modsen.passengerservice.dto;

import lombok.Builder;
import org.springframework.http.MediaType;

import java.io.InputStream;

@Builder(setterPrefix = "with")
public record MinioFileInformation(
        InputStream is,
        MediaType mediaType) {
}
