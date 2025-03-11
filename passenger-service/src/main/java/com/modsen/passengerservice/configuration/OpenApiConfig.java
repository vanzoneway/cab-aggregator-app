package com.modsen.passengerservice.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Passenger Service API",
                description = "One of the services from the cab aggregator app", version = "1.0.0",
                contact = @Contact(
                        name = "Ivan Zinovich",
                        email = "vanz.evergarden0@gmail.com"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8086", description = "Local server")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {
}

