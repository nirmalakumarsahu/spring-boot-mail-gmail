package com.sahu.mailservice.service.impl;

import com.sahu.mailservice.dto.EmailTemplateRequest;
import com.sahu.mailservice.dto.EmailTemplateResponse;
import com.sahu.mailservice.model.EmailTemplate;
import com.sahu.mailservice.repository.EmailTemplateRepository;
import com.sahu.mailservice.service.EmailTemplateService;
import com.sahu.mailservice.service.util.EmailTemplateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailTemplateServiceImpl implements EmailTemplateService {

    private final EmailTemplateRepository emailTemplateRepository;

    @Override
    public EmailTemplateResponse addEmailTemplate(EmailTemplateRequest emailTemplateRequest, MultipartFile file) {
        //Validate the Template file
        return null;
    }

    @Override
    public List<EmailTemplateResponse> getAllEmailTemplates() {
        return EmailTemplateUtil.toEmailTemplateResponseList(emailTemplateRepository.findAll());
    }
}
