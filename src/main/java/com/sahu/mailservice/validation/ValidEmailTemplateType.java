package com.sahu.mailservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailTemplateTypeValidator.class)
public @interface ValidEmailTemplateType {
    String message() default "Invalid email template type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
