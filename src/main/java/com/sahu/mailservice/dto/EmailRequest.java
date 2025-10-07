package com.sahu.mailservice.dto;

import java.util.List;
import java.util.Map;

public record EmailRequest(
        List<String> to,
        List<String> cc,
        List<String> bcc,
        String subject,
        String body,
        boolean isHtml,
        String templateId,
        Map<String, Object> templateModel
) {
}
