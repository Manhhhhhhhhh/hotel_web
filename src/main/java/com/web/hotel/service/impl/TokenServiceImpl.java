package com.web.hotel.service.impl;

import com.nimbusds.jwt.SignedJWT;
import com.web.hotel.model.entity.UserEntity;
import com.web.hotel.service.TokenService;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private SecretKey secretKey;

    @Override
    public String generateToken(UserEntity user) {
        String name = user.getName();
        String role = user.getRole().getRoleName();
        JWTClaimsSet claimSet = new JWTClaimsSet.Builder()
                .claim("roles", Collections.singleton(role))
                .subject(name)
                .expirationTime(new Date(new Date().getTime() + 60 * 60 * 24 * 1000)) // 1 day
                .build();
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimSet);
        try {
            JWSSigner signer = new MACSigner(secretKey);
            signedJWT.sign(signer);
            return signedJWT.serialize();// token
        } catch (Exception e) {
            return "failed to generate token";
        }
    }

    @Override
    public String generateRefreshToken(UserEntity user) {
        String name = user.getName();
        String role = user.getRole().getRoleName();
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .claim("roles", Collections.singleton(role))
                .subject(name)
                .expirationTime(new Date(new Date().getTime() + 60 * 60 * 24 * 1009 * 7))
                .build();
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        try{
            JWSSigner signer = new MACSigner(secretKey);
            signedJWT.sign(signer);
            return signedJWT.serialize();
        }catch (Exception e){
            return "failed to generate refresh token";
        }
    }
}
