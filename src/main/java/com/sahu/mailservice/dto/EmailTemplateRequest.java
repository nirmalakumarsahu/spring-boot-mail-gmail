package com.sahu.mailservice.dto;

import com.sahu.mailservice.validation.ValidEmailTemplateType;
import jakarta.validation.constraints.NotBlank;

public record EmailTemplateRequest(
        @NotBlank(message = "Name is required")
        String name,
        @ValidEmailTemplateType
        String type,
        @NotBlank(message = "Subject is required")
        String subject
)
{

}
