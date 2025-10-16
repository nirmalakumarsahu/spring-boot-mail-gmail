package com.sahu.mailservice.dto;

import com.sahu.mailservice.validation.ValidEmailRequest;

import java.util.List;
import java.util.Map;

@ValidEmailRequest
public record EmailRequest(
        List<String> to,
        List<String> cc,
        List<String> bcc,
        String subject,
        String body,
        boolean isHtml,
        String templateName,
        Map<String, Object> templateModel
) {
}
