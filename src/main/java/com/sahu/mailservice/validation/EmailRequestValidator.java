package com.sahu.mailservice.validation;

import com.sahu.mailservice.dto.EmailRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailRequestValidator implements ConstraintValidator<ValidEmailRequest, EmailRequest> {

    /*
     List<String> to,
        List<String> cc,
        List<String> bcc,
        String subject,
        String body,
        boolean isHtml,
        String templateId,
        Map<String, Object> templateModel
    * */
    @Override
    public boolean isValid(EmailRequest emailRequest, ConstraintValidatorContext context) {
        if(Object.is)

        return false;
    }

}
