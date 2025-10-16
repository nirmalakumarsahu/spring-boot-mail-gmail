package com.sahu.mailservice.service.impl;

import com.sahu.mailservice.constant.EmailTemplateType;
import com.sahu.mailservice.dto.EmailRequest;
import com.sahu.mailservice.dto.EmailResponse;
import com.sahu.mailservice.model.EmailTemplate;
import com.sahu.mailservice.repository.EmailTemplateRepository;
import com.sahu.mailservice.service.SendEmailService;
import freemarker.template.Configuration;
import freemarker.template.Template;
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

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendEmailServiceImpl implements SendEmailService {

    private final EmailTemplateRepository emailTemplateRepository;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final Configuration freemarkerConfig;

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

        if (Objects.nonNull(emailRequest.templateName())) {
            String content = getContentFomTemplate(emailRequest);
            log.info("Email content generated from template: {}", content);
            messageHelper.setText(content, true);
        } else {
            messageHelper.setText(emailRequest.body(), emailRequest.isHtml());
        }

        //Adding the files
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

    private String getContentFomTemplate(EmailRequest emailRequest) {
        log.info("Processing template with id: {}", emailRequest.templateName());
        EmailTemplate emailTemplate =  emailTemplateRepository.findByNameAndActiveTrue(emailRequest.templateName())
                .orElseThrow(() -> new IllegalArgumentException("Email template with name " + emailRequest.templateName() + " not found"));

        //check the required placeholders are present in the template model
        if (emailTemplate.getPlaceholders() != null && !emailTemplate.getPlaceholders().isEmpty()) {
            List<String> missingPlaceholders = emailTemplate.getPlaceholders().stream()
                    .filter(placeholder -> !emailRequest.templateModel().containsKey(placeholder))
                    .toList();
            if (!missingPlaceholders.isEmpty()) {
                throw new IllegalArgumentException("Missing placeholders in template model: " + String.join(", ", missingPlaceholders));
            }
        }

        if (emailTemplate.getType().equals(EmailTemplateType.THYMELEAF)) {
            Context context = new Context();
            context.setVariables(emailRequest.templateModel());
            return templateEngine.process(emailTemplate.getContent(), context);
        }
        else if (emailTemplate.getType().equals(EmailTemplateType.FREEMARKER)) {
            try {
                Template freemarkerTemplate = new Template(
                        emailTemplate.getName(),
                        emailTemplate.getContent(),
                        freemarkerConfig
                );

                StringWriter stringWriter = new StringWriter();
                freemarkerTemplate.process(emailRequest.templateModel(), stringWriter);
                return stringWriter.toString();
            } catch (Exception e) {
                log.error("Error while processing FreeMarker template: {}", e.getMessage());
                throw new IllegalStateException("Error while processing FreeMarker template", e);
            }
        }

        throw new IllegalArgumentException("Unsupported email template type: " + emailTemplate.getType());
    }

}
