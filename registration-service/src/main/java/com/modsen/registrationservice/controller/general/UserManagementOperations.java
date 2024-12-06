package com.modsen.registrationservice.controller.general;

import com.modsen.registrationservice.dto.SignInAdminDto;
import com.modsen.registrationservice.dto.SignInDto;
import com.modsen.registrationservice.dto.SignUpDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

@Validated
public interface UserManagementOperations {

    ResponseEntity<String> signIn(@Valid @RequestBody SignInDto signInDto);

    void signUp(@Valid @RequestBody SignUpDto signUpDto);

    ResponseEntity<String> signInAsAdmin(@Valid @RequestBody SignInAdminDto signInAdminDto);

}
