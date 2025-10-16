package com.sahu.mailservice.service.util;

import com.sahu.mailservice.constant.EmailTemplateType;
import com.sahu.mailservice.dto.EmailTemplateRequest;
import com.sahu.mailservice.dto.EmailTemplateResponse;
import com.sahu.mailservice.model.EmailTemplate;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@UtilityClass
public class EmailTemplateUtil {

    public EmailTemplate toEmailTemplate(EmailTemplateRequest emailTemplateRequest, MultipartFile file) {
        return EmailTemplate.builder()
                .name(emailTemplateRequest.name())
                .type(EmailTemplateType.valueOf(emailTemplateRequest.type()))
                .content(getFileContent(file))
                .placeholders(getPlaceholders(EmailTemplateType.valueOf(emailTemplateRequest.type()), file))
                .build();
    }

    public EmailTemplateResponse toEmailTemplateResponse(EmailTemplate emailTemplate) {
        return EmailTemplateResponse.builder()
                .id(emailTemplate.getId())
                .name(emailTemplate.getName())
                .subject(emailTemplate.getSubject())
                .type(emailTemplate.getType())
                .placeholders(emailTemplate.getPlaceholders())
                .active(emailTemplate.getActive())
                .build();
    }

    public List<EmailTemplateResponse> toEmailTemplateResponseList(List<EmailTemplate> emailTemplates) {
        return emailTemplates.stream()
                .map(EmailTemplateUtil::toEmailTemplateResponse)
                .toList();
    }

    public void validateEmailTemplate(String templateType, MultipartFile file) {
        Assert.notNull(templateType, "Template type must not be null");
        Assert.notNull(file, "File must not be null");

        switch (EmailTemplateType.valueOf(templateType)) {
            case THYMELEAF -> validateThymeleafTemplate(file);
            case FREEMARKER -> validateFreemarkerTemplate(file);
            default -> throw new IllegalArgumentException("Unsupported email template type: " + templateType);
        }
    }

    private static void validateFreemarkerTemplate(MultipartFile file) {
        log.info("Validating Freemarker template: {}", file.getOriginalFilename());
        String filename = file.getOriginalFilename();
        Assert.notNull(filename, "File name must not be null");
        Assert.isTrue(filename.endsWith(".ftl"), "Invalid file type. Expected .ftl file for Freemarker template");
    }

    private static void validateThymeleafTemplate(MultipartFile file) {
        log.info("Validating Thymeleaf template: {}", file.getOriginalFilename());
        String filename = file.getOriginalFilename();
        Assert.notNull(filename, "File name must not be null");
        Assert.isTrue(filename.endsWith(".html") || filename.endsWith(".htm"),
                "Invalid file type. Expected .html or .htm file for Thymeleaf template");
    }

    private String getFileContent(MultipartFile file) {
        try {
            return new String(file.getBytes());
        }
        catch (IOException e) {
            log.error("Error while reading file content: {}", e.getMessage());
            throw new IllegalArgumentException("Error while reading file content");
        }
    }

    private List<String> getPlaceholders(EmailTemplateType emailTemplateType, MultipartFile file) {
        Assert.notNull(file, "File must not be null");

       switch (emailTemplateType) {
              case THYMELEAF -> {
                  //read the file content and extract placeholders and check the passed placeholders are present in the file content
                return List.of("{{name}}", "{{date}}");
              }
              case FREEMARKER -> {
                return List.of("${name}", "${date}");
              }
              default -> throw new IllegalArgumentException("Unsupported email template type: " + emailTemplateType);
       }
    }

}
