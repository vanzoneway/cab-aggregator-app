package com.modsen.passengerservice.dto;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record AvatarImageDto(
        String url,
        String bucketObjectName) {
}
