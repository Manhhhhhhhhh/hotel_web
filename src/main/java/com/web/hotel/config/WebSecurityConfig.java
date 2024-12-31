package com.web.hotel.config;

import com.web.hotel.util.MyCustomSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        return new DefaultOAuth2UserService();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, MyCustomSuccessHandler successHandler) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> {
                    request
                            .requestMatchers("/login",
                                    "/forgot_password**",
                                    "/refresh_token",
                                    "/register",
                                    "/resend**",
                                    "/reset_password",
                                    "/verify",
                                    "/comment/**"
                            ).permitAll()
                            .requestMatchers("/hotel/**").permitAll()
                            .requestMatchers("/hotel").permitAll()
                            .requestMatchers("/admin/**").hasRole("ADMIN")
                            .requestMatchers("/user/**").hasRole("USER")
                            .anyRequest().authenticated();
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(oauth2 ->
                        oauth2
                                .successHandler(successHandler)
                                .authorizationEndpoint(authorizationEndpoint ->
                                        authorizationEndpoint.baseUri("/login/oauth2/authorization"))
                                .redirectionEndpoint(redirectionEndpoint
                                        -> redirectionEndpoint.baseUri("/login/oauth2/code/*"))
                                .tokenEndpoint(token
                                        -> token.accessTokenResponseClient(new DefaultAuthorizationCodeTokenResponseClient()))
                                .userInfoEndpoint(userInfo
                                        -> userInfo.userService(this.oauth2UserService()))
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(
                                jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                ;
        return http.build();
    }
}
