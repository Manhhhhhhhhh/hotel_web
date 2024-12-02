package com.web.hotel.service;

public interface EmailService {
    public void sendVerificationEmail(String to, String subject, String text);
}
