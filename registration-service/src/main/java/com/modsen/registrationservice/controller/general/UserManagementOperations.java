package com.modsen.registrationservice.controller.general;

import com.modsen.registrationservice.dto.AdminKeycloakTokenResponseDto;
import com.modsen.registrationservice.dto.SignInAdminDto;
import com.modsen.registrationservice.dto.SignInDto;
import com.modsen.registrationservice.dto.SignUpDto;
import com.modsen.registrationservice.dto.UserKeycloakTokenResponseDto;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

@Validated
public interface UserManagementOperations {

    UserKeycloakTokenResponseDto signIn(@Valid @RequestBody SignInDto signInDto);

    void signUp(@Valid @RequestBody SignUpDto signUpDto);

    AdminKeycloakTokenResponseDto signInAsAdmin(@Valid @RequestBody SignInAdminDto signInAdminDto);

}
