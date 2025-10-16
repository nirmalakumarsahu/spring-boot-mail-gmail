package com.sahu.mailservice.service.impl;

import com.sahu.mailservice.constant.EmailTemplateType;
import com.sahu.mailservice.dto.EmailTemplateRequest;
import com.sahu.mailservice.dto.EmailTemplateResponse;
import com.sahu.mailservice.exception.EmailTemplateAlreadyExistsException;
import com.sahu.mailservice.model.EmailTemplate;
import com.sahu.mailservice.repository.EmailTemplateRepository;
import com.sahu.mailservice.service.EmailTemplateService;
import com.sahu.mailservice.service.util.EmailTemplateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.Thymeleaf;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailTemplateServiceImpl implements EmailTemplateService {

    private final EmailTemplateRepository emailTemplateRepository;

    @Override
    public EmailTemplateResponse addEmailTemplate(EmailTemplateRequest emailTemplateRequest, MultipartFile file) {
        //Check if email template with same name already exists
        if (emailTemplateRepository.existsByNameAndActiveTrue(emailTemplateRequest.name())) {
            throw new EmailTemplateAlreadyExistsException("Email template with name " + emailTemplateRequest.name() + " already exists");
        }

        //Validate and upload file
        EmailTemplateUtil.validateEmailTemplate(emailTemplateRequest.type(), file);

        EmailTemplate emailTemplate = emailTemplateRepository.save(EmailTemplateUtil.toEmailTemplate(emailTemplateRequest, file));
        return EmailTemplateUtil.toEmailTemplateResponse(emailTemplate);
    }

    @Override
    public List<EmailTemplateResponse> getAllEmailTemplates() {
        return EmailTemplateUtil.toEmailTemplateResponseList(emailTemplateRepository.findAll());
    }

    @Override
    public void deleteEmailTemplate(Long id) {
        emailTemplateRepository.findById(id).ifPresentOrElse(
                emailTemplate -> {
                    emailTemplate.setActive(false);
                    emailTemplateRepository.save(emailTemplate);
                },
                () -> {
                    throw new IllegalArgumentException("Email template with id " + id + " not found");
                }
        );
    }

}
