package com.sahu.mailservice.validation;

import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailRequestValidator.class)
public @interface ValidEmailRequest {
    String message() default "Invalid email request";

    Class<?>[] groups() default {};

    Class<? extends jakarta.validation.Payload>[] payload() default {};
}
