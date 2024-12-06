package com.modsen.registrationservice.dto;

public record TokenResponseDto(String token,
                               String refreshToken) {
}
