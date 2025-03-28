package com.modsen.gatewayservice.configuration;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "General cab-aggregator-app API documentation",
                description = """
                    This documentation provides general information about all microservices that have APIs for interaction with end users.
                    To switch between services, select a specific service from the "Select definition" dropdown in the upper right corner.
                    """,
                version = "1.0.0",
                contact = @Contact(
                        name = "Ivan Zinovich",
                        email = "vanz.evergarden0@gmail.com"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local server"),
                @Server(url = "http://cab-aggregator.ddns.net:8080", description = "Test server")
        }
)
public class OpenApiConfig {
}