package com.vanya.validation.validators;

import com.vanya.validation.annotations.ValidEmail;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class EmailValidator implements ConstraintValidator<ValidEmail, String> {
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern PATTERN = Pattern.compile(EMAIL_PATTERN);

    @Override
    public void initialize(ValidEmail constraintAnnotation) {

    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return (validateEmail(email));
    }

    private boolean validateEmail(final String email) {
        if (!StringUtils.isBlank(email)) {
            Matcher matcher = PATTERN.matcher(email);
            return matcher.matches();
        }
        return false;
    }
}
