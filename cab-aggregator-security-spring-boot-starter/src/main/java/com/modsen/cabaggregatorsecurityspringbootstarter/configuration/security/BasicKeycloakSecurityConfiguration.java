package com.modsen.cabaggregatorsecurityspringbootstarter.configuration.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@ConditionalOnProperty(name = "spring.security.oauth2.resourceserver.jwt.issuer-uri")
@ConditionalOnMissingBean(BasicKeycloakSecurityConfiguration.class)
public class BasicKeycloakSecurityConfiguration {

    private static final String ACTUATOR_ENDPOINT = "/actuator/**";
    private static final String PASSENGER_STATISTICS_ENDPOINT = "api/v1/rides/passengers/*/statistics";
    private static final String DRIVER_STATISTICS_ENDPOINT = "api/v1/rides/drivers/*/statistics";

    @Bean
    @ConditionalOnMissingBean(SecurityFilterChain.class)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(c -> c
                        .requestMatchers(ACTUATOR_ENDPOINT).permitAll()
                        .requestMatchers(PASSENGER_STATISTICS_ENDPOINT).permitAll()
                        .requestMatchers(DRIVER_STATISTICS_ENDPOINT).permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(configurer ->
                        configurer
                                .jwt(jwt ->
                                        jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(CsrfConfigurer::disable)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean(JwtAuthenticationConverter.class)
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakJwtTokenConverter());
        return converter;
    }
}
