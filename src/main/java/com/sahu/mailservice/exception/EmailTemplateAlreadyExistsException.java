package com.sahu.mailservice.exception;

public class EmailTemplateAlreadyExistsException extends RuntimeException {
    public EmailTemplateAlreadyExistsException(String message) {
        super(message);
    }
}
