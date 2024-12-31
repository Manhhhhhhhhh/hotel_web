package com.web.hotel.api;


import com.web.hotel.model.dto.*;
import com.web.hotel.model.response.CommentResponse;
import com.web.hotel.model.response.Hotel;
import com.web.hotel.model.response.HotelResponse;
import com.web.hotel.service.AuthenticationService;
import com.web.hotel.service.CommentService;
import com.web.hotel.service.HotelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class HomeAPI {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/hotel")
    public List<HotelResponse> findByRequestParam(@ModelAttribute HotelDTO hotelDTO) {
        return hotelService.findByRequestParam(hotelDTO);
    }

    @GetMapping("/hotel/{id}")
    public HotelResponse findById(@PathVariable Long id) {
        return hotelService.findById(id);
    }

    @GetMapping("/comment/{hotelId}")
    public List<CommentResponse> findByHotelId(@PathVariable Long hotelId) {
        return commentService.findByHotelId(hotelId);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserLoginDTO userLoginDTO,
                                        BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid username or password");
        }
        String token = authenticationService.login(userLoginDTO);
        return ResponseEntity.ok("login successful, token: " + token);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO,
                                           BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("invalid login information");
        }
        if(!userRegisterDTO.getRetype().equals(userRegisterDTO.getPassword())){
            return ResponseEntity.badRequest().body("retype dose not match");
        }
        authenticationService.register(userRegisterDTO);
        return ResponseEntity.ok("register successful");
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verify(@RequestBody VerificationDTO verificationDTO){
        return ResponseEntity.ok(authenticationService.verify(verificationDTO));
    }

    @GetMapping("/resend")
    public ResponseEntity<String> resend(@RequestParam("email") String email){
        return ResponseEntity.ok(authenticationService.resend(email));
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<String> refreshToken(@RequestBody RefreshTokenDTO refreshTokenDTO){
        String token = authenticationService.getAccessToken(refreshTokenDTO);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/reset_password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO){
        return ResponseEntity.ok(authenticationService.resetPassword(resetPasswordDTO));
    }
}
