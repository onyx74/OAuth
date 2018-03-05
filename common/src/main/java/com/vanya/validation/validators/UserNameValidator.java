package com.vanya.validation.validators;

import com.vanya.validation.annotations.ValidUserName;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UserNameValidator implements ConstraintValidator<ValidUserName, String> {
    @Override
    public void initialize(ValidUserName constraintAnnotation) {

    }

    @Override
    public boolean isValid(String userName, ConstraintValidatorContext context) {
        return !StringUtils.isBlank(userName) && userName.length() > 3;
    }
}
