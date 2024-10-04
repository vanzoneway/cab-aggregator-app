package com.modsen.passengerservice.dto;

import lombok.Builder;

import java.util.List;

@Builder(setterPrefix = "with")
public record ListContainerResponseDto<T>(
        int currentOffset,
        int currentLimit,
        int totalPages,
        long totalElements,
        String sort,
        List<T> values) {
}
