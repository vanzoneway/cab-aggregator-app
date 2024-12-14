package com.modsen.registrationservice.dto;

import com.modsen.registrationservice.constants.AppConstants;
import com.modsen.registrationservice.exception.validation.AccessRoles;
import com.modsen.registrationservice.exception.validation.EnumTypeSubset;
import com.modsen.registrationservice.exception.validation.GenderTypes;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;

public record SignUpDto(

        @NotBlank(message = "{firstname.empty}")
        String firstName,

        @NotBlank(message = "{lastname.empty}")
        String lastName,

        @Email(message = "{email.invalid}")
        @NotBlank(message = "{email.empty}")
        String email,

        @NotBlank(message = "{empty.password}")
        String password,

        @NotBlank(message = "{phone.empty}")
        @Pattern(message = "{phone.invalid}", regexp = AppConstants.PHONE_PATTERN)
        String phone,

        @NotBlank(message = "{empty.gender}")
        @EnumTypeSubset(enumClass = GenderTypes.class, message = "{wrong.gender}")
        String gender,

        @EnumTypeSubset(enumClass = AccessRoles.class, message = "{role.wrong.enum.type}")
        String role) implements Serializable {
}