package com.sahu.mailservice.controll.rest;

import com.sahu.mailservice.dto.ApiResponse;
import com.sahu.mailservice.dto.EmailTemplateRequest;
import com.sahu.mailservice.dto.EmailTemplateResponse;
import com.sahu.mailservice.service.EmailTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/email-templates")
@RequiredArgsConstructor
public class EmailTemplateRestController {

    private final EmailTemplateService emailTemplateService;

    @PostMapping
    public ResponseEntity<ApiResponse<EmailTemplateResponse>> addEmailTemplate(@RequestPart EmailTemplateRequest emailTemplateRequest,
                                                                               @RequestPart(required = true) MultipartFile file)
    {
        return ApiResponse.success(
                HttpStatus.CREATED,
                "Email template added successfully",
                emailTemplateService.addEmailTemplate(emailTemplateRequest, file)
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EmailTemplateResponse>>> getAllEmailTemplates() {
        return ApiResponse.success(
                HttpStatus.OK,
                "Email templates fetched successfully",
                emailTemplateService.getAllEmailTemplates()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteEmailTemplate(@PathVariable Long id) {
        // Implementation for deleting an email template by id
        return ApiResponse.success(
                HttpStatus.OK,
                "Email template deleted successfully",
                null
        );
    }

}
