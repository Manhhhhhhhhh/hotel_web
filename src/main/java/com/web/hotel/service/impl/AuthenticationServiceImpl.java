package com.web.hotel.service.impl;

import com.web.hotel.exception.MailErrorException;
import com.web.hotel.exception.UserErrorException;
import com.web.hotel.model.dto.*;
import com.web.hotel.model.entity.MyFavouriteEntity;
import com.web.hotel.model.entity.RefreshTokenEntity;
import com.web.hotel.model.entity.RoleEntity;
import com.web.hotel.model.entity.UserEntity;
import com.web.hotel.repository.RefreshTokenRepository;
import com.web.hotel.repository.RoleRepository;
import com.web.hotel.repository.UserRepository;
import com.web.hotel.service.AuthenticationService;
import com.web.hotel.service.EmailService;
import com.web.hotel.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private EmailService emailService;

    @Override
    public String login(UserLoginDTO userLoginDTO) {
        String username = userLoginDTO.getUsername();
        Optional<UserEntity> optional =userRepository.findByUsername(username);
        // username, password
        if(optional.isEmpty()) {
            throw new UsernameNotFoundException("username not found");
        }
        UserEntity user = optional.get();
        if(!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("wrong password");
        }
        if(!user.isEnable()){
            throw new UserErrorException("user is not enabled");
        }
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userLoginDTO.getUsername(), userLoginDTO.getPassword(), user.getAuthorities());
        authenticationManager.authenticate(authentication);
        // refreshToken
        String refreshToken = tokenService.generateRefreshToken(user);
        RefreshTokenEntity refreshTokenEntity =  RefreshTokenEntity
                .builder()
                .refreshToken(refreshToken)
                .build();
        refreshTokenRepository.deleteByUser(user);
        user.getRefreshTokenEntities().add(refreshTokenEntity);
        userRepository.save(user);
        // token
        return tokenService.generateToken(user);
    }

    @Override
    public void register(UserRegisterDTO userRegisterDTO) {
        String username = userRegisterDTO.getUsername();
        Optional<UserEntity> optional = userRepository.findByUsername(username);
        if(optional.isPresent()) {
            throw new UserErrorException("username already in use");
        }
        Optional<UserEntity> optionalEmail = userRepository.findByEmail(userRegisterDTO.getEmail());
        if(optionalEmail.isPresent()) {
            throw new UserErrorException("email already in use");
        }
        Long roleId = userRegisterDTO.getRoleId();
        if(roleId != 2) {
            throw new UserErrorException("not authorized role");
        }
        RoleEntity role = roleRepository.findById(roleId).get();

        UserEntity user = UserEntity.builder()
                .role(role)
                .email(userRegisterDTO.getEmail())
                .name(userRegisterDTO.getName())
                .username(username)
                .password(passwordEncoder.encode(userRegisterDTO.getPassword()))
                .verificationCode(generateVerificationCode())
                .verificationCodeExpire(new Date(new Date().getTime() + 15 * 60 * 1000))
                .commentEntities(new ArrayList<>())
                .dealEntities(new ArrayList<>())
                .fileEntities(new ArrayList<>())
                .refreshTokenEntities(new ArrayList<>())
                .build();

        MyFavouriteEntity myFavouriteEntity = MyFavouriteEntity.builder()
                .user(user)
                .hotels(new ArrayList<>())
                .build();
        ArrayList<MyFavouriteEntity> myFavouriteEntities = new ArrayList<>();
        myFavouriteEntities.add(myFavouriteEntity);
        user.setMyFavouriteEntities(myFavouriteEntities);


        if(userRegisterDTO.getPhone() != null) {
            user.setPhone(userRegisterDTO.getPhone());
        }
        if(userRegisterDTO.getAge() != null) {
            user.setAge(userRegisterDTO.getAge());
        }
        sendVerificationCode(user);
        role.getUsers().add(user);
        roleRepository.save(role);
        userRepository.save(user);
    }

    @Override
    public String verify(VerificationDTO verificationDTO) {
        String email = verificationDTO.getEmail();
        String verificationCode = verificationDTO.getVerificationCode();
        Optional<UserEntity> optional = userRepository.findByEmail(email);
        if(optional.isEmpty()) {
            return "not found user email";
        }
        UserEntity user = optional.get();
        if (user.isEnable()) {
            return "account is already verified";
        }
        if(!user.getVerificationCode().equals(verificationCode) || !new Date().before(user.getVerificationCodeExpire())) {
            return "fail to verify";
        }
        user.setEnable(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpire(null);
        userRepository.save(user);
        return "success verification";
    }

    @Override
    public String resend(String email) {
        Optional<UserEntity> optional = userRepository.findByEmail(email);
        if(optional.isEmpty()) {
            return "not found user email";
        }
        String verificationCode = generateVerificationCode();
        UserEntity user = optional.get();
        if(user.isEnable()){
            return "user already verified";
        }
        user.setVerificationCodeExpire(new Date(new Date().getTime() + 15 * 60 * 1000));
        user.setVerificationCode(verificationCode);
        userRepository.save(user);
        sendVerificationCode(user);
        return "send verification code successfully";
    }

    @Override
    public String getAccessToken(RefreshTokenDTO refreshTokenDTO) {
        if(!tokenService.validateRefreshToken(refreshTokenDTO)){
            return "";
        }
        Optional<UserEntity> optional = userRepository.findByUsername(refreshTokenDTO.getUsername());
        if(optional.isEmpty()) {
            return "";
        }
        UserEntity user = optional.get();
        return tokenService.generateToken(user);
    }

    @Override
    public String resetPassword(ResetPasswordDTO resetPasswordDTO) {
        String email = resetPasswordDTO.getEmail();
        String resetCode = resetPasswordDTO.getResetCode();
        String newPassword = resetPasswordDTO.getNewPassword();
        Optional<UserEntity>  optional = userRepository.findByEmail(email);
        if(optional.isEmpty()) {
            return "User not found";
        }
        UserEntity user = optional.get();
        if(!user.getResetCode().equals(resetCode) || !new Date().before(user.getResetCodeExpiration())) {
            return "failed to reset password";
        }
        user.setResetCode(null);
        user.setResetCodeExpiration(null);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return "reset password successfully";
    }

    public String generateVerificationCode(){
        Random random  = new Random();
        int code = random.nextInt(10000);
        return String.valueOf(code);
    }

    public void sendVerificationCode(UserEntity user){
        String email = user.getEmail();
        String verificationCode = user.getVerificationCode();
        String subject = "Account Verification";
        String html = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
        try{
            emailService.sendVerificationEmail(email, subject, html);
        }catch (Exception e){
            throw new MailErrorException("failed to send verification email");
        }
    }
}
