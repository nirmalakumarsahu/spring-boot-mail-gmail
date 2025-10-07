package com.sahu.mailservice.service;

import com.sahu.mailservice.dto.EmailRequest;
import com.sahu.mailservice.dto.EmailResponse;
import jakarta.mail.MessagingException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SendEmailService {
    EmailResponse sendEmail(EmailRequest emailRequest, List<MultipartFile> files) throws MessagingException;
}
