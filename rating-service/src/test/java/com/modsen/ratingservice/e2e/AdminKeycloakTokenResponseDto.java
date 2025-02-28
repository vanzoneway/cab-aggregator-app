package com.modsen.ratingservice.e2e;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public record AdminKeycloakTokenResponseDto(
        @JsonProperty("access_token")
        String accessToken,

        @JsonProperty("expires_in")
        Integer expiresIn,

        @JsonProperty("refresh_expires_in")
        Integer refreshExpiresIn,

        @JsonProperty("token_type")
        String tokenType,

        @JsonProperty("not_before_policy")
        Integer notBeforePolicy,

        String scope) implements Serializable {
}
