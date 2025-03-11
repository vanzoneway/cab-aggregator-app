package com.modsen.registrationservice.controller.general;

import com.modsen.registrationservice.dto.AdminKeycloakTokenResponseDto;
import com.modsen.registrationservice.dto.SignInAdminDto;
import com.modsen.registrationservice.dto.SignInDto;
import com.modsen.registrationservice.dto.SignUpDto;
import com.modsen.registrationservice.dto.UserKeycloakTokenResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

@Validated
@Tag(name = "user-management-operations", description = """
        The endpoints contained here are intended for operations related to user management. For example: signing in as a user,
        signing up as a new user, and signing in as an admin.

        It is important to note that these endpoints interact with Keycloak for authentication and authorization.
        """)
@SecurityRequirement(name = "bearerAuth")
public interface UserManagementOperations {

    @Operation(summary = "Sign in as a user",
            description = """
                    Authenticates a user and returns a JWT token for accessing protected resources.

                    Required fields:
                    - **email**: The user's email address (valid format)
                    - **password**: The user's password (non-empty string)

                    Validation rules:
                    - `email` must be a valid email format and not empty.
                    - `password` must not be blank.

                    Example request:
                    {
                        "email": "user@example.com",
                        "password": "password123"
                    }

                    Example response:
                    {
                        "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                        "expires_in": 3600,
                        "refresh_expires_in": 1800,
                        "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                        "token_type": "Bearer",
                        "not_before_policy": 0,
                        "session_state": "f0e8c7a0-5b1a-4b2a-8c1a-5b1a4b2a8c1a",
                        "scope": "profile email"
                    }
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User signed in successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden, invalid credentials"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    UserKeycloakTokenResponseDto signIn(@Valid @RequestBody SignInDto signInDto);

    @Operation(summary = "Sign up as a new user",
            description = """
                    Registers a new user in the system and assigns them a role (PASSENGER or DRIVER).

                    Required fields:
                    - **firstName**: The user's first name (non-empty string)
                    - **lastName**: The user's last name (non-empty string)
                    - **email**: The user's email address (valid format)
                    - **password**: The user's password (non-empty string)
                    - **phone**: The user's phone number (valid format)
                    - **gender**: The user's gender (MALE or FEMALE)
                    - **role**: The user's role (PASSENGER or DRIVER)

                    Validation rules:
                    - `firstName` and `lastName` must not be blank.
                    - `email` must be a valid email format and not empty.
                    - `password` must not be blank.
                    - `phone` must match the pattern: +[country code][number] (7-15 digits total).
                    - `gender` must be either MALE or FEMALE.
                    - `role` must be either PASSENGER or DRIVER.

                    Example request:
                    {
                        "firstName": "John",
                        "lastName": "Doe",
                        "email": "john.doe@example.com",
                        "password": "password123",
                        "phone": "+1234567890",
                        "gender": "MALE",
                        "role": "PASSENGER"
                    }

                    Example response:
                    - Status Code: 201 (Created)
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User signed up successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Conflict, user already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    void signUp(@Valid @RequestBody SignUpDto signUpDto);

    @Operation(summary = "Sign in as an admin",
            description = """
                    Authenticates an admin and returns a JWT token for accessing protected resources.

                    Required fields:
                    - **grantType**: The grant type (must be "client_credentials")
                    - **clientId**: The client ID for the admin application
                    - **clientSecret**: The client secret for the admin application

                    Validation rules:
                    - `grantType` must be "client_credentials".
                    - `clientId` must not be blank.
                    - `clientSecret` must not be blank.

                    Example request:
                    {
                        "grantType": "client_credentials",
                        "clientId": "admin-client",
                        "clientSecret": "admin-secret"
                    }

                    Example response:
                    {
                        "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                        "expires_in": 3600,
                        "refresh_expires_in": 1800,
                        "token_type": "Bearer",
                        "not_before_policy": 0,
                        "scope": "profile email"
                    }
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Admin signed in successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden, invalid credentials"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    AdminKeycloakTokenResponseDto signInAsAdmin(@Valid @RequestBody SignInAdminDto signInAdminDto);

}
