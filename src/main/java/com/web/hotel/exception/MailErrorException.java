package com.web.hotel.exception;

public class MailErrorException extends RuntimeException {
    public MailErrorException(String message) {
        super(message);
    }
}
