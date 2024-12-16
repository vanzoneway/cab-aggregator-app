package com.modsen.registrationservice.configuration;

import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class KeycloakConfiguration {

    private final KeycloakProperties keycloakProperties;

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakProperties.getUserManagement().getServerUrl())
                .realm(keycloakProperties.getRealm())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(keycloakProperties.getUserManagement().getClientId())
                .clientSecret(keycloakProperties.getUserManagement().getClientSecret())
                .username(keycloakProperties.getUserManagement().getUsername())
                .password(keycloakProperties.getUserManagement().getPassword())
                .build();
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
