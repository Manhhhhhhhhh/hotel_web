package com.web.hotel.service.impl;

import com.nimbusds.jwt.SignedJWT;
import com.web.hotel.model.dto.RefreshTokenDTO;
import com.web.hotel.model.entity.UserEntity;
import com.web.hotel.repository.UserRepository;
import com.web.hotel.service.TokenService;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private SecretKey secretKey;

    @Autowired
    private UserRepository userRepository;

    @Override
    public String generateToken(UserEntity user) {
        String name = user.getUsername();
        String role = user.getRole().getRoleName();
        JWTClaimsSet claimSet = new JWTClaimsSet.Builder()
                .claim("roles", Collections.singleton(role))
                .subject(name)
                .expirationTime(new Date(new Date().getTime() + 60 * 60 * 1000)) // after 1 h
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
        String name = user.getUsername();
        String role = user.getRole().getRoleName();
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .claim("roles", Collections.singleton(role))
                .subject(name)
                .expirationTime(new Date(new Date().getTime() + 60 * 60 * 24 * 1000 )) // after 1 days
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

    @Override
    public boolean validateRefreshToken(RefreshTokenDTO refreshTokenDTO) {
        String username = refreshTokenDTO.getUsername();
        String refreshToken = refreshTokenDTO.getRefreshToken();
        Optional<UserEntity> optional = userRepository.findByUsername(username);
        if(optional.isEmpty()){
            return false;
        }
        UserEntity user = optional.get();
        String storedRefreshToken = user.getRefreshTokenEntities().getFirst().getRefreshToken();
        if(!refreshToken.equals(storedRefreshToken)){
            return false;
        }
        try{
            SignedJWT signedJWT = SignedJWT.parse(refreshToken);
            JWSVerifier verifier = new MACVerifier(secretKey);
            return signedJWT.verify(verifier) && new Date().before(signedJWT.getJWTClaimsSet().getExpirationTime());
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public String getUsername(String token) {
        try{
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            return claimsSet.getSubject();
        } catch (Exception e) {
            return "not found";
        }
    }
}
