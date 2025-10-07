package com.sahu.mailservice.service.util;

import com.sahu.mailservice.constant.EmailTemplateType;
import com.sahu.mailservice.dto.EmailTemplateRequest;
import com.sahu.mailservice.dto.EmailTemplateResponse;
import com.sahu.mailservice.model.EmailTemplate;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class EmailTemplateUtil {

    public EmailTemplate toEmailTemplate(EmailTemplateRequest emailTemplateRequest, String content, List<String> placeholders) {
        return EmailTemplate.builder()
                .name(emailTemplateRequest.name())
                .subject(emailTemplateRequest.subject())
                .type(EmailTemplateType.valueOf(emailTemplateRequest.type()))
                .content(content)
                .placeholders(placeholders)
                .build();
    }

    public EmailTemplateResponse toEmailTemplateResponse(EmailTemplate emailTemplate) {
        return EmailTemplateResponse.builder()
                .id(emailTemplate.getId())
                .name(emailTemplate.getName())
                .subject(emailTemplate.getSubject())
                .type(emailTemplate.getType())
                .placeholders(emailTemplate.getPlaceholders())
                .active(emailTemplate.getActive())
                .build();
    }

    public List<EmailTemplateResponse> toEmailTemplateResponseList(List<EmailTemplate> emailTemplates) {
        return emailTemplates.stream()
                .map(EmailTemplateUtil::toEmailTemplateResponse)
                .toList();
    }

}
