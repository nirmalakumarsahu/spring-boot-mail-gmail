package com.sahu.mailservice.controll.rest;

import com.sahu.mailservice.dto.ApiResponse;
import com.sahu.mailservice.dto.EmailRequest;
import com.sahu.mailservice.dto.EmailResponse;
import com.sahu.mailservice.service.SendEmailService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/emails")
@RequiredArgsConstructor
public class EmailRestController {

    private final SendEmailService sendEmailService;

    @PostMapping(value = "/send")
    public ResponseEntity<ApiResponse<EmailResponse>> sendEmail(@Valid @RequestPart EmailRequest emailRequest,
                                                                @RequestPart(required = false) List<MultipartFile> files)
    {
        try {
            EmailResponse emailResponse = sendEmailService.sendEmail(emailRequest, files);
            return ApiResponse.success(
                    HttpStatus.OK,
                    "Email sent successfully",
                    emailResponse
            );
        } catch (MessagingException e) {
            return  ApiResponse.failure(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to send email",
                    e.getMessage()
            );
        }
    }
}
