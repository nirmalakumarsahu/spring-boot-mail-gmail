package com.sahu.mailservice.dto;

import com.sahu.mailservice.constant.EmailTemplateType;
import lombok.Builder;

import java.util.List;

@Builder
public record EmailTemplateResponse(
        Long id,
        String name,
        String subject,
        String content,
        EmailTemplateType type,
        List<String> placeholders,
        Boolean active
)
{
}
