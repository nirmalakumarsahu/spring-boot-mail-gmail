package com.sahu.mailservice.dto;

import lombok.Builder;

@Builder
public record EmailResponse(
        String message,
        Boolean status
) {
}
