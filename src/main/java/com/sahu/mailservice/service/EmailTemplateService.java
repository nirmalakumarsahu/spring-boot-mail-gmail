package com.sahu.mailservice.service;

import com.sahu.mailservice.dto.EmailTemplateRequest;
import com.sahu.mailservice.dto.EmailTemplateResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EmailTemplateService {
    EmailTemplateResponse addEmailTemplate(EmailTemplateRequest emailTemplateRequest, MultipartFile file);

    List<EmailTemplateResponse> getAllEmailTemplates();
}
