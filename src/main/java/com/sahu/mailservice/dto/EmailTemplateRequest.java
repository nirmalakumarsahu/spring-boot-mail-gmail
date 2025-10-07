package com.sahu.mailservice.dto;

public record EmailTemplateRequest(
        String name,
        String subject,
        String type
)
{
}
