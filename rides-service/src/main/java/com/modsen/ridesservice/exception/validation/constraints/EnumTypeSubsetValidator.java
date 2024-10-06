package com.modsen.ridesservice.exception.validation.constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumTypeSubsetValidator implements ConstraintValidator<EnumTypeSubset, Object> {

    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(EnumTypeSubset constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        for (Enum<?> e : enumClass.getEnumConstants()) {
            if (e.name().equals(value)) {
                return true;
            }
        }
        return false;
    }

}
