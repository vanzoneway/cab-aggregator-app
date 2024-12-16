package com.modsen.registrationservice.service;

import com.modsen.registrationservice.dto.AdminKeycloakTokenResponseDto;
import com.modsen.registrationservice.dto.SignInAdminDto;
import com.modsen.registrationservice.dto.SignInDto;
import com.modsen.registrationservice.dto.SignUpDto;
import com.modsen.registrationservice.dto.UserKeycloakTokenResponseDto;
import org.springframework.http.ResponseEntity;

public interface UserManagementService {

    void signUp(SignUpDto signUpDto);

    UserKeycloakTokenResponseDto signIn(SignInDto signInDto);

    AdminKeycloakTokenResponseDto signInAsAdmin(SignInAdminDto signInDto);

}
