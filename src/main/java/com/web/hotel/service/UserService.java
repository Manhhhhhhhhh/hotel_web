package com.web.hotel.service;

import com.web.hotel.model.dto.CommentDTO;
import com.web.hotel.model.dto.DealDTO;
import com.web.hotel.model.dto.MyFavouriteDTO;
import com.web.hotel.model.dto.UserInfoUpdateDTO;
import com.web.hotel.model.response.DealResponse;
import com.web.hotel.model.response.MyFavouriteResponse;
import com.web.hotel.model.response.UserResponse;

import java.util.List;

public interface UserService {
    public List<UserResponse> getUsers();
    public String updateUserInfo(UserInfoUpdateDTO userInfoUpdateDTO);

    public UserResponse userInfo(String authorization);
    public String createComment(CommentDTO commentDTO);
    public String deleteComment(Long id);

    public List<DealResponse> getDeals(Long id);
    public String createDeal(DealDTO dealDTO);
    public String editDeal(DealDTO dealDTO);
    public String deleteDeal(Long id);

    public String addToMyFavourites(MyFavouriteDTO myFavouriteDTO);
    public MyFavouriteResponse getMyFavourites(Long userId);
    public String deleteMyFavourites(MyFavouriteDTO myFavouriteDTO);
}
