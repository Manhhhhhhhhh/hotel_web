package com.web.hotel.util;

import com.web.hotel.model.entity.UserEntity;
import com.web.hotel.repository.UserRepository;
import com.web.hotel.service.TokenService;
import com.web.hotel.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class MyCustomSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
        String email = user.getAttribute("email");
        Optional<UserEntity> optional =userRepository.findByEmail(email);
        if (optional.isPresent()) {
            UserEntity userEntity = optional.get();
            String token = tokenService.generateToken(userEntity);
            response.setContentType("application/json");
            response.getWriter().write("{\"accessToken\": \"" + token + "\"}");
            response.getWriter().flush();
        }
        else response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
