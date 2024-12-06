package com.modsen.registrationservice.service;

import com.modsen.registrationservice.dto.SignInAdminDto;
import com.modsen.registrationservice.dto.SignInDto;
import com.modsen.registrationservice.dto.SignUpDto;
import org.springframework.http.ResponseEntity;

public interface UserManagementService {

    void signUp(SignUpDto signUpDto);

    ResponseEntity<String> signIn(SignInDto signInDto);

    ResponseEntity<String> signInAsAdmin(SignInAdminDto signInDto);

}
