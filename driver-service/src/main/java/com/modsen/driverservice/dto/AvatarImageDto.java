package com.modsen.driverservice.dto;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record AvatarImageDto(
        String url,
        String bucketObjectName) {
}
