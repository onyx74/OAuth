package com.vanya.validation.annotations;


import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


import com.vanya.validation.validators.UserNameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;


@Retention(RUNTIME)
@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Constraint(validatedBy = UserNameValidator.class)
@Documented
public @interface ValidUserName {
    String message() default "User name should be at least four symbol";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
