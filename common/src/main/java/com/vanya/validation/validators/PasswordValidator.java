package com.vanya.validation.validators;


import com.google.common.base.Joiner;
import com.vanya.validation.annotations.ValidPassword;
import org.passay.*;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

@Component
public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {
    private static final org.passay.PasswordValidator VALIDATOR = new org.passay.PasswordValidator(Arrays.asList(
            new LengthRule(6, 30)
//            new UppercaseCharacterRule(1)
//            new DigitCharacterRule(1),
//            new SpecialCharacterRule(1),
//            new NumericalSequenceRule(3, false),
//            new AlphabeticalSequenceRule(3, false),
//            new QwertySequenceRule(3, false)
                                                                                                                ));

    @Override
    public void initialize(ValidPassword constraintAnnotation) {

    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        // @formatter:off

        final RuleResult result = VALIDATOR.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(Joiner.on(",").join(VALIDATOR.getMessages(result))).addConstraintViolation();
        return false;
    }
}
