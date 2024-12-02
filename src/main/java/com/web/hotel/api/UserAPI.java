package com.web.hotel.api;

import com.web.hotel.exception.DealErrorException;
import com.web.hotel.model.dto.DealDTO;
import com.web.hotel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
public class UserAPI {

    @Autowired
    private UserService userService;

    @PostMapping("/deal")
    public ResponseEntity<String> deal(@RequestBody DealDTO dealDTO){
        try{

        }catch (Exception e){
            throw new DealErrorException("Deal Error");
        }
        return null;
    }
}
