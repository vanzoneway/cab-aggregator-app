package com.modsen.registrationservice.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "keycloak")
@Data
public class KeycloakProperties {

    private String realm;
    private UserManagement userManagement;
    private Auth auth;

    @Data
    public static class UserManagement {
        private String clientId;
        private String clientSecret;
        private String username;
        private String password;
        private String serverUrl;
    }

    @Data
    public static class Auth {
        private String clientId;
    }

}
