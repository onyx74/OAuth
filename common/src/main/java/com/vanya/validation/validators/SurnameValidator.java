package com.vanya.validation.validators;

import com.vanya.validation.annotations.ValidSurname;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class SurnameValidator implements ConstraintValidator<ValidSurname, String> {
    @Override
    public void initialize(ValidSurname constraintAnnotation) {

    }

    @Override
    public boolean isValid(String surname, ConstraintValidatorContext context) {
        return !StringUtils.isBlank(surname) && surname.length() > 3;
    }
}
