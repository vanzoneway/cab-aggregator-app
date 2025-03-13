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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@ConditionalOnProperty(name = "spring.security.oauth2.resourceserver.jwt.issuer-uri")
@ConditionalOnMissingBean(BasicKeycloakSecurityConfiguration.class)
public class BasicKeycloakSecurityConfiguration {

    private static final String ACTUATOR_ENDPOINT = "/actuator/**";
    private static final String PASSENGER_STATISTICS_ENDPOINT = "api/v1/rides/passengers/*/statistics";
    private static final String DRIVER_STATISTICS_ENDPOINT = "api/v1/rides/drivers/*/statistics";

    private static final String SWAGGER_UI_ENDPOINT = "/swagger-ui/**";
    private static final String SWAGGER_API_DOCS_ENDPOINT = "/v3/api-docs/**";

    @Bean
    @ConditionalOnMissingBean(SecurityFilterChain.class)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(c -> c
                        .requestMatchers(ACTUATOR_ENDPOINT).permitAll()
                        .requestMatchers(PASSENGER_STATISTICS_ENDPOINT).permitAll()
                        .requestMatchers(DRIVER_STATISTICS_ENDPOINT).permitAll()
                        .requestMatchers(SWAGGER_UI_ENDPOINT).permitAll()
                        .requestMatchers(SWAGGER_API_DOCS_ENDPOINT).permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(configurer ->
                        configurer
                                .jwt(jwt ->
                                        jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(CsrfConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .build();
    }

    @Bean
    @ConditionalOnMissingBean(JwtAuthenticationConverter.class)
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakJwtTokenConverter());
        return converter;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
