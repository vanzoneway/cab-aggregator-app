package com.modsen.ridesservice.exception.validation;

import com.modsen.ridesservice.constants.AppConstants;
import com.modsen.ridesservice.exception.validation.provider.EnumTypeSubsetValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {EnumTypeSubsetValidator.class})
public @interface EnumTypeSubset {
    Class<? extends Enum<?>> enumClass();

    String message() default AppConstants.ENUM_TYPE_INVALID_MESSAGE_KEY;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
