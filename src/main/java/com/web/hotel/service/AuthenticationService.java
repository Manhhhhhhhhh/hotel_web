package com.web.hotel.service;

import com.web.hotel.model.dto.*;

public interface AuthenticationService {
    public String login(UserLoginDTO userLoginDTO);
    public void register(UserRegisterDTO userRegisterDTO);
    public String verify(VerificationDTO verificationDTO);
    public String resend(String email);
    public String getAccessToken(RefreshTokenDTO refreshTokenDTO);
    public String resetPassword(ResetPasswordDTO resetPasswordDTO);
}
