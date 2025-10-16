package com.sahu.mailservice.validation;

import com.sahu.mailservice.constant.EmailTemplateType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class EmailTemplateTypeValidator implements ConstraintValidator<ValidEmailTemplateType, String> {

    @Override
    public boolean isValid(String emailTemplateType, ConstraintValidatorContext context) {
        try {
            if (Objects.isNull(emailTemplateType) || emailTemplateType.isBlank()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Email template type is required")
                        .addConstraintViolation();
                return false;
            }

            //Email template type must be either THYMELEAF or FREEMARKER
            EmailTemplateType.valueOf(emailTemplateType);
            return true;
        }
        catch (IllegalArgumentException | NullPointerException ex) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Invalid email template type. Allowed values are: THYMELEAF, FREEMARKER")
                    .addConstraintViolation();
            return false;
        }
    }

}
