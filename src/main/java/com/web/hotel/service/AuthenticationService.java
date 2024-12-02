package com.web.hotel.service;

import com.web.hotel.model.dto.UserLoginDTO;
import com.web.hotel.model.dto.UserRegisterDTO;
import com.web.hotel.model.dto.VerificationDTO;

public interface AuthenticationService {
    public String login(UserLoginDTO userLoginDTO);
    public void register(UserRegisterDTO userRegisterDTO);
    public String verify(VerificationDTO verificationDTO);
    public String resend(String email);
}
