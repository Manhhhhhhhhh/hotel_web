package com.web.hotel.service.impl;

import com.web.hotel.exception.MailErrorException;
import com.web.hotel.exception.UserErrorException;
import com.web.hotel.model.entity.UserEntity;
import com.web.hotel.repository.UserRepository;
import com.web.hotel.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void sendVerificationEmail(String to, String subject, String text) {
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom("nguyenmanhlc10@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new MailErrorException("error sending verification email");
        }
    }

    @Override
    public void sendResetPasswordEmail(String to) {
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            String resetCode = generateResetCode();
            String resetLink = "http://localhost:8081/reset_password";

            Optional<UserEntity> optional = userRepository.findByEmail(to);
            if(optional.isEmpty()){
                throw new UserErrorException("User not found");
            }
            UserEntity user = optional.get();
            user.setResetCode(resetCode);
            user.setResetCodeExpiration(new Date(new Date().getTime() + 15 * 60 * 1000));
            userRepository.save(user);

            helper.setFrom("nguyenmanhlc10@gmail.com");
            helper.setTo(to);
            helper.setSubject("Reset password");
            helper.setText("Reset code: " + resetCode + ", Reset password: " + resetLink);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new MailErrorException("error sending reset code email");
        }
    }

    public String generateResetCode(){
        Random random = new Random();
        return random.nextInt(10000000) + "";
    }
}
