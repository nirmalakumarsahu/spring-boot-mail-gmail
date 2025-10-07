package com.sahu.mailservice.service.impl;

import com.sahu.mailservice.dto.EmailRequest;
import com.sahu.mailservice.dto.EmailResponse;
import com.sahu.mailservice.service.SendEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendEmailServiceImpl implements SendEmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public EmailResponse sendEmail(EmailRequest emailRequest, List<MultipartFile> files) throws MessagingException {
        Assert.notNull(emailRequest, "Mail Request cannot be null");

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        messageHelper.setFrom(fromEmail);
        messageHelper.setTo(emailRequest.to().toArray(new String[0]));

        if (Objects.nonNull(emailRequest.cc())) {
            messageHelper.setCc(emailRequest.cc().toArray(new String[0]));
        }

        if (Objects.nonNull(emailRequest.bcc())) {
            messageHelper.setBcc(emailRequest.bcc().toArray(new String[0]));
        }

        messageHelper.setSubject(emailRequest.subject());
        messageHelper.setSentDate(new Date());

        if (Objects.nonNull(emailRequest.templateId())) {
            Context context = new Context();
            context.setVariables(emailRequest.templateModel());
            messageHelper.setText(templateEngine.process(emailRequest.templateId(), context), true);
        } else {
            messageHelper.setText(emailRequest.body(), emailRequest.isHtml());
        }

        if (Objects.nonNull(files) && !files.isEmpty()) {
            files.forEach(file -> {
                try {
                    messageHelper.addAttachment(
                            Objects.requireNonNull(file.getOriginalFilename()),
                            new ByteArrayResource(file.getBytes())
                    );
                } catch (Exception e) {
                    log.error("Unable to add File {} reason {}", file.getOriginalFilename(), e.getMessage());
                }
            });
        }

        javaMailSender.send(mimeMessage);

        return EmailResponse.builder()
                .message("Mail Sent Successfully")
                .status(true)
                .build();
    }

}
