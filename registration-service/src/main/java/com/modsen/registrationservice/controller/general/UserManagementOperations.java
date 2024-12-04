package com.modsen.registrationservice.controller.general;

import com.modsen.registrationservice.dto.SignInDto;
import com.modsen.registrationservice.dto.SignUpDto;
import org.springframework.http.ResponseEntity;

public interface UserManagementOperations {

    ResponseEntity<String> signIn(SignInDto signInDto);

    void signUp(SignUpDto signUpDto);

}
