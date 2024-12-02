package com.web.hotel.api;


import com.web.hotel.model.dto.HotelDTO;
import com.web.hotel.model.dto.UserLoginDTO;
import com.web.hotel.model.dto.UserRegisterDTO;
import com.web.hotel.model.dto.VerificationDTO;
import com.web.hotel.model.response.HotelResponse;
import com.web.hotel.service.AuthenticationService;
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
    private AuthenticationService authenticationService;

    @GetMapping("/hotel")
    public List<HotelResponse> findByRequestParam(@ModelAttribute HotelDTO hotelDTO) {
        return hotelService.findByRequestParam(hotelDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserLoginDTO userLoginDTO,
                                        BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid username or password");
        }
        authenticationService.login(userLoginDTO);
        return ResponseEntity.ok("login successful");
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

    @PostMapping("/resend")
    public ResponseEntity<String> resend(@RequestBody String email){
        return ResponseEntity.ok(authenticationService.resend(email));
    }
}
