package com.vanya.validation.annotations;

import com.vanya.validation.validators.SurnameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = SurnameValidator.class)
@Documented
public @interface ValidSurname{
    String message() default "Invalid surname";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}