package com.vanya.validation.validators;

import com.vanya.validation.annotations.ValidFirstName;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class FirstNameValidator implements ConstraintValidator<ValidFirstName, String> {
    @Override
    public void initialize(ValidFirstName constraintAnnotation) {

    }

    @Override
    public boolean isValid(String firstName, ConstraintValidatorContext context) {
        return !StringUtils.isBlank(firstName) && firstName.length() > 3;
    }
}
