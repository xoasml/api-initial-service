package com.hoon.api.utils.exception;

public class MailSendException extends RuntimeException{
    public MailSendException(String message) {
        super(message);
    }
}
