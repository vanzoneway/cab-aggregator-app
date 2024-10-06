package com.modsen.ridesservice.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Rides Service API",
                description = "One of the services from the cab aggregator app", version = "1.0.0",
                contact = @Contact(
                        name = "Ivan Zinovich",
                        email = "vanz.evergarden0@gmail.com"
                )
        )
)
public class OpenApiConfig {
}
