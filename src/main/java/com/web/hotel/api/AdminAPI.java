package com.web.hotel.api;

import com.web.hotel.model.dto.DealDTO;
import com.web.hotel.model.dto.HotelDTO;
import com.web.hotel.model.entity.UserEntity;
import com.web.hotel.model.response.DealResponse;
import com.web.hotel.model.response.HotelResponse;
import com.web.hotel.model.response.UserResponse;
import com.web.hotel.service.DealService;
import com.web.hotel.service.HotelService;
import com.web.hotel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin")
public class AdminAPI {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private UserService userService;

    @Autowired
    private DealService dealService;

    @PostMapping("/create_hotel/{userId}")
    public ResponseEntity<String> create(@ModelAttribute HotelDTO hotelDTO, @PathVariable Long userId) {
        return ResponseEntity.ok(hotelService.create(hotelDTO, userId));
    }

    @PostMapping("/update_hotel")
    public ResponseEntity<String> update(@ModelAttribute HotelDTO hotelDTO){
        return ResponseEntity.ok(hotelService.update(hotelDTO));
    }

    @DeleteMapping("/delete_hotel/{hotelId}")
    public ResponseEntity<String> delete(@PathVariable Long hotelId){
        return ResponseEntity.ok(hotelService.delete(hotelId));
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserResponse>> getUser(){
        return ResponseEntity.ok(userService.getUsers());
    }


    // admin id
    @GetMapping("/hotel_management")
    public ResponseEntity<List<HotelResponse>> getHotel(@RequestParam Long adminId){
        return ResponseEntity.ok(hotelService.getHotelsManagement(adminId));
    }

    @GetMapping("/deal_management")
    public ResponseEntity<List<DealResponse>> getDeal(@RequestParam Long hotelId){
        return ResponseEntity.ok(dealService.getDeal(hotelId));
    }


    // deal DTO gui 2 gia tri id va status
    @PostMapping("/deal_management")
    public ResponseEntity<String> deal(@RequestBody DealDTO dealDTO){
        return ResponseEntity.ok(dealService.deal(dealDTO));
    }
}
