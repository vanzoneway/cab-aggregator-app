package com.modsen.registrationservice.controller.impl;

import com.modsen.registrationservice.controller.general.UserManagementOperations;
import com.modsen.registrationservice.dto.AdminKeycloakTokenResponseDto;
import com.modsen.registrationservice.dto.SignInAdminDto;
import com.modsen.registrationservice.dto.SignInDto;
import com.modsen.registrationservice.dto.SignUpDto;
import com.modsen.registrationservice.dto.UserKeycloakTokenResponseDto;
import com.modsen.registrationservice.service.UserManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cab-aggregator")
@RequiredArgsConstructor
public class UserManagementController implements UserManagementOperations {

    private final UserManagementService userManagementService;

    @Override
    @PostMapping("/signin")
    public UserKeycloakTokenResponseDto signIn(@Valid @RequestBody SignInDto signInDto) {
        return userManagementService.signIn(signInDto);
    }

    @Override
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@Valid @RequestBody SignUpDto signUpDto) {
        userManagementService.signUp(signUpDto);
    }

    @Override
    @PostMapping("/signin/admin")
    public AdminKeycloakTokenResponseDto signInAsAdmin(@Valid @RequestBody SignInAdminDto signInAdminDto) {
        return userManagementService.signInAsAdmin(signInAdminDto);
    }

}
