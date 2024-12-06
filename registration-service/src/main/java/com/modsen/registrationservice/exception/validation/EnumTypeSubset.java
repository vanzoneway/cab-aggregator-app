package com.modsen.registrationservice.exception.validation;

import com.modsen.registrationservice.constants.AppConstants;
import com.modsen.registrationservice.exception.validation.provider.EnumTypeSubsetValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {EnumTypeSubsetValidator.class})
public @interface EnumTypeSubset {

    Class<? extends Enum<?>> enumClass();

    String message() default AppConstants.ENUM_TYPE_INVALID_MESSAGE_KEY;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
