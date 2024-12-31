package com.web.hotel.api;

import com.web.hotel.exception.MailErrorException;
import com.web.hotel.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class New {

    @Autowired
    private EmailService emailService;

    @GetMapping("/forgot_password")
    public ResponseEntity<String> forgotPassword(@RequestParam("email") String email) {
        try{
            emailService.sendResetPasswordEmail(email);
            return ResponseEntity.ok("Check your email");
        } catch (Exception e) {
            throw new MailErrorException("Failed to send email");
        }
    }
}
