package com.sahu.mailservice.validation;

import com.sahu.mailservice.dto.EmailRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class EmailRequestValidator implements ConstraintValidator<ValidEmailRequest, EmailRequest> {

    @Override
    public boolean isValid(EmailRequest emailRequest, ConstraintValidatorContext context) {
        log.info("Validating email request: {}", emailRequest);
        try {
            //to is required
            if (Objects.isNull(emailRequest.to()) || emailRequest.to().isEmpty()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("At least one recipient (to) must be provided")
                        .addPropertyNode("to")
                        .addConstraintViolation();
                return false;
            }

            //subject is required
            if (Objects.isNull(emailRequest.subject()) || emailRequest.subject().isBlank()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Subject must be provided")
                        .addPropertyNode("subject")
                        .addConstraintViolation();
                return false;
            }

            //Validate template id and body logic
            if (Objects.nonNull(emailRequest.body()) && !emailRequest.body().isBlank()) {
                //body is provided, templateId and templateModel must be null
                if (Objects.nonNull(emailRequest.templateName()) || Objects.nonNull(emailRequest.templateModel())) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("If body is provided, Template name and templateModel must be null")
                            .addPropertyNode("body")
                            .addConstraintViolation();
                    return false;
                }
            } else {
                //body is not provided, templateId must be provided
               return checkForTemplateNameAndModel(emailRequest, context);
            }

            return true;
        }
        catch (Exception ex) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Error while validating email request: " + ex.getMessage())
                    .addConstraintViolation();
            return false;
        }
    }

    private boolean checkForTemplateNameAndModel(EmailRequest emailRequest, ConstraintValidatorContext context) {
        if (Objects.isNull(emailRequest.templateName()) || emailRequest.templateName().isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("If body is not provided, Template name must be provided")
                    .addPropertyNode("templateId")
                    .addConstraintViolation();
            return false;
        }

        if(Objects.isNull(emailRequest.templateModel()) || emailRequest.templateModel().isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("If body is not provided, templateModel must be provided")
                    .addPropertyNode("templateModel")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

}
