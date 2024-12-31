package com.web.hotel.service;

import com.web.hotel.model.dto.RefreshTokenDTO;
import com.web.hotel.model.entity.UserEntity;

public interface TokenService {
    public String generateToken(UserEntity user);

    public String generateRefreshToken(UserEntity user);

    public boolean validateRefreshToken(RefreshTokenDTO refreshTokenDTO);

    public String getUsername(String token);
}
