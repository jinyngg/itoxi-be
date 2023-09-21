package com.itoxi.petnuri.global.common.customValid.validator;

import com.itoxi.petnuri.global.common.customValid.valid.ValidPetName;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PetNameValidator implements ConstraintValidator<ValidPetName, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.matches("^[a-zA-Z0-9]*$") && value.length() >= 1 && value.length() <= 10;
    }
}
