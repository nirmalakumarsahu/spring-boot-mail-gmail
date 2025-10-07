package com.sahu.mailservice.exception;

import com.sahu.mailservice.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailTemplateAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<String>> handleEmailTemplateAlreadyExistsException(EmailTemplateAlreadyExistsException emailTemplateAlreadyExistsException)
    {
        return buildErrorResponse(HttpStatus.CONFLICT, emailTemplateAlreadyExistsException.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException)
    {
        Map<String, String> fieldErrors = methodArgumentNotValidException.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> Optional.ofNullable(fieldError.getDefaultMessage())
                                .orElse("Invalid Values")
                ));

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", fieldErrors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGenericException(Exception exception) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), null);
    }

    private ResponseEntity<ApiResponse<String>> buildErrorResponse(HttpStatus httpStatus, String message, Object error) {
        return ApiResponse.error(
                httpStatus,
                message,
                error
        );
    }

}
