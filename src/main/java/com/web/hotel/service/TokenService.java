package com.web.hotel.service;

import com.web.hotel.model.entity.UserEntity;

public interface TokenService {
    public String generateToken(UserEntity user);

    public String generateRefreshToken(UserEntity user);
}
