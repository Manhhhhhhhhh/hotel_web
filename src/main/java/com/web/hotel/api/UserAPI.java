package com.web.hotel.api;

import com.nimbusds.openid.connect.sdk.UserInfoResponse;
import com.web.hotel.exception.DealErrorException;
import com.web.hotel.model.dto.CommentDTO;
import com.web.hotel.model.dto.DealDTO;
import com.web.hotel.model.dto.MyFavouriteDTO;
import com.web.hotel.model.dto.UserInfoUpdateDTO;
import com.web.hotel.model.entity.DealEntity;
import com.web.hotel.model.response.DealResponse;
import com.web.hotel.model.response.MyFavouriteResponse;
import com.web.hotel.model.response.UserResponse;
import com.web.hotel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/user")
@RestController
public class UserAPI {

    @Autowired
    private UserService userService;

    // api deal
    @PostMapping("/create-deal")
    public ResponseEntity<String> createDeal(@RequestBody DealDTO dealDTO){
        try{
            return ResponseEntity.ok(userService.createDeal(dealDTO));
        }catch (Exception e){
            throw new DealErrorException("Deal Error");
        }
    }

    @GetMapping("/deals")
    public ResponseEntity<List<DealResponse>> getDeals(@RequestParam(name = "id") Long id){
        return ResponseEntity.ok(userService.getDeals(id));
    }

    @PostMapping("/edit-deal")
    public ResponseEntity<String> editDeal(@RequestBody DealDTO dealDTO){
        return ResponseEntity.ok(userService.editDeal(dealDTO));
    }

    @DeleteMapping("/delete-deal/{dealId}")
    public ResponseEntity<String> deleteDeal(@PathVariable Long dealId){
        return ResponseEntity.ok(userService.deleteDeal(dealId));
    }

    // api comment
    @PostMapping("/comment")
    public ResponseEntity<String> createComment(@RequestBody CommentDTO commentDTO){
        try{
            return ResponseEntity.ok(userService.createComment(commentDTO));
        }catch (Exception e){
            return ResponseEntity.ok("Fail to create comment");
        }
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId){
        try{
            return ResponseEntity.ok(userService.deleteComment(commentId));
        }catch (Exception e){
            return ResponseEntity.ok("Fail to delete comment");
        }
    }
    // api myFavourite

    @PostMapping("/myFavourite")
    public ResponseEntity<String> addMyFavourite(@RequestBody MyFavouriteDTO myFavouriteDTO){
        return ResponseEntity.ok(userService.addToMyFavourites(myFavouriteDTO));
    }

    @GetMapping("/myFavourite")
    public ResponseEntity<MyFavouriteResponse> getMyFavourites(@RequestParam Long userId){
        return ResponseEntity.ok(userService.getMyFavourites(userId));
    }

    @DeleteMapping("/myFavourite")
    public ResponseEntity<String> deleteMyFavourite(@RequestBody MyFavouriteDTO myFavouriteDTO){
        return ResponseEntity.ok(userService.deleteMyFavourites(myFavouriteDTO));
    }

    // api user info
    @GetMapping("/info")
    public ResponseEntity<UserResponse> info(@RequestHeader("Authorization") String authorization){
        return ResponseEntity.ok(userService.userInfo(authorization));
    }

    @PostMapping("/info")
    public ResponseEntity<String> info(@ModelAttribute UserInfoUpdateDTO userInfoUpdateDTO){
        try{
            return ResponseEntity.ok(userService.updateUserInfo(userInfoUpdateDTO));
        } catch (Exception e) {
            return ResponseEntity.ok("Fail to update user info");
        }
    }
}
