package com.web.hotel.service.impl;

import com.web.hotel.convert.DealConvert;
import com.web.hotel.convert.MyFavouriteConvert;
import com.web.hotel.convert.UserConvert;
import com.web.hotel.exception.*;
import com.web.hotel.model.dto.CommentDTO;
import com.web.hotel.model.dto.DealDTO;
import com.web.hotel.model.dto.MyFavouriteDTO;
import com.web.hotel.model.dto.UserInfoUpdateDTO;
import com.web.hotel.model.entity.*;
import com.web.hotel.model.response.DealResponse;
import com.web.hotel.model.response.MyFavouriteResponse;
import com.web.hotel.model.response.UserResponse;
import com.web.hotel.repository.*;
import com.web.hotel.service.CloudinaryService;
import com.web.hotel.service.TokenService;
import com.web.hotel.service.UserService;
import com.web.hotel.util.DateUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final TokenService tokenService;

    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;
    private final FileRepository fileRepository;
    private final HotelRepository hotelRepository;
    private final CommentRepository commentRepository;
    private final DealRepository dealRepository;
    private final MyFavouriteRepository myFavouriteRepository;

    private final DealConvert dealConvert;
    private final UserConvert userConvert;
    private final MyFavouriteConvert myFavouriteConvert;


    @Override
    public String updateUserInfo(UserInfoUpdateDTO userInfoUpdateDTO) {
        Long id = userInfoUpdateDTO.getId();
        UserEntity user = userRepository.findById(id).get();
        String name = userInfoUpdateDTO.getName();
        String phone = userInfoUpdateDTO.getPhone();
        Long age = userInfoUpdateDTO.getAge();
        MultipartFile file = userInfoUpdateDTO.getFile();
        if(name != null){
            user.setName(name);
        }
        if(phone != null){
            user.setPhone(phone);
        }
        if(age != null){
            user.setAge(age);
        }
        if(file != null){
            Map res = cloudinaryService.upload(file);
            FileEntity fileEntity =  FileEntity.builder()
                    .fileUrl((String)res.get("original_filename"))
                    .fileId((String)res.get("public_id"))
                    .fileName((String)res.get("name"))
                    .build();
            List<FileEntity> fileEntities = user.getFileEntities();
            if(fileEntities != null){
                fileRepository.deleteByUser(user);
            }
            fileEntity.setUser(user);
            fileEntities.add(fileEntity);
            user.setFileEntities(fileEntities);
        }
        userRepository.save(user);
        return "changed successfully";
    }


    @Override
    public UserResponse userInfo(String authorization) {
        String token = authorization.substring(7);
        String username = tokenService.getUsername(token);
        Optional<UserEntity> optional = userRepository.findByUsername(username);
        if(optional.isEmpty()){
            return null;
        }
        UserEntity user = optional.get();
        return userConvert.convertTo(user);
    }

    @Override
    public String createComment(CommentDTO commentDTO) {
        Optional<UserEntity> optional = userRepository.findById(commentDTO.getUserId());
        if(optional.isEmpty()){
            return "not found user";
        }
        Optional<HotelEntity> optional1 = hotelRepository.findById(commentDTO.getHotelId());
        if(optional1.isEmpty()){
            return "not found hotel";
        }
        if(commentDTO.getContent() == null){
            throw new CommentErrorException("comment content is null");
        }
        HotelEntity hotelEntity = optional1.get();
        UserEntity userEntity = optional.get();
        CommentEntity commentEntity = CommentEntity.builder()
                .content(commentDTO.getContent())
                .user(userEntity)
                .hotel(hotelEntity)
                .build();
        hotelEntity.getCommentEntities().add(commentEntity);
        userEntity.getCommentEntities().add(commentEntity);

        commentRepository.save(commentEntity);

        return "create comment successfully";
    }

    @Override
    public List<UserResponse> getUsers() {
        List<UserEntity> users = userRepository.findAll();
        List<UserResponse> userResponses = new ArrayList<>();
        for(UserEntity user : users){
            userResponses.add(userConvert.convertTo(user));
        }
        return userResponses;
    }

    @Override
    public String deleteComment(Long id) {
        Optional<CommentEntity> commentEntity = commentRepository.findById(id);
        if(commentEntity.isEmpty()){
            return "not found comment";
        }
        commentRepository.deleteById(id);
        return "deleted successfully";
    }

    @Override
    public List<DealResponse> getDeals(Long id) {
        List<DealEntity> dealEntities = dealRepository.findByUser_Id(id);
        if(dealEntities.isEmpty()){
            return null;
        }
        List<DealResponse> dealResponses = new ArrayList<>();
        for(DealEntity dealEntity : dealEntities){
            dealResponses.add(dealConvert.convertToDealResponse(dealEntity));
        }
        return dealResponses;
    }

    @Override
    public String createDeal(DealDTO dealDTO) {
        UserEntity userEntity = userRepository.findById(dealDTO.getUserId()).get();
        HotelEntity hotelEntity = hotelRepository.findById(dealDTO.getHotelId()).get();
        if(dealDTO.getNumberOfRoom() == null){
            throw new DealErrorException("number of room is null");
        }
        if(hotelEntity.getNumberOfRoom() < dealDTO.getNumberOfRoom()){
            return "not enough room";
        }
        if(dealDTO.getStart() == null || dealDTO.getEnd() == null){
            throw new DealErrorException("start and end is null");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        DealEntity dealEntity = DealEntity.
                builder()
                .numberOfRoom(dealDTO.getNumberOfRoom())
                .start(DateUtil.convertToDate(dealDTO.getStart()))
                .end(DateUtil.convertToDate(dealDTO.getEnd()))
                .user(userEntity)
                .hotel(hotelEntity)
                .status("processing")
                .build();
        userEntity.getDealEntities().add(dealEntity);
        hotelEntity.getDealEntities().add(dealEntity);

        dealRepository.save(dealEntity);
        return "create deal successfully";
    }

    @Override
    public String editDeal(DealDTO dealDTO) {
        DealEntity dealEntity = dealRepository.findById(dealDTO.getId()).get();
        if(dealDTO.getNumberOfRoom() == null){
            throw new DealErrorException("number of room is null");
        }
        dealEntity.setNumberOfRoom(dealDTO.getNumberOfRoom());
        if(dealDTO.getStart() == null || dealDTO.getEnd() == null){
            throw new DealErrorException("start and end is null");
        }
        Date start = DateUtil.convertToDate(dealDTO.getStart());
        Date end = DateUtil.convertToDate(dealDTO.getEnd());
        if(end.before(start)){
            throw new DealErrorException("start is before end");
        }
        dealEntity.setStart(start);
        dealEntity.setEnd(end);

        dealRepository.save(dealEntity);
        return "edit deal successfully";
    }

    @Override
    public String deleteDeal(Long id) {
        Optional<DealEntity> dealEntity = dealRepository.findById(id);
        if(dealEntity.isEmpty()){
            return "not found deal";
        }
        dealRepository.deleteById(id);
        return "deleted deal successfully";
    }

    @Override
    public String addToMyFavourites(MyFavouriteDTO myFavouriteDTO) {
        Optional<HotelEntity> optionalHotel = hotelRepository.findById(myFavouriteDTO.getHotelId());
        if(optionalHotel.isEmpty()){
            throw new HotelErrorException("hotel not found");
        }
        HotelEntity hotelEntity = optionalHotel.get();
        Optional<MyFavouriteEntity> optionalMyFavouriteEntity = myFavouriteRepository.findByUser_Id(myFavouriteDTO.getUserId());
        if(optionalMyFavouriteEntity.isEmpty()){
            throw new MyFavouriteErrorException("my favourite not found");
        }

        MyFavouriteEntity myFavouriteEntity = optionalMyFavouriteEntity.get();
        myFavouriteEntity.getHotels().add(hotelEntity);
        hotelEntity.getMyFavouriteEntities().add(myFavouriteEntity);
        myFavouriteRepository.save(myFavouriteEntity);
        hotelRepository.save(hotelEntity);

        return "add to MyFavourites successfully";
    }

    @Override
    public MyFavouriteResponse getMyFavourites(Long userId) {
        Optional<MyFavouriteEntity> optional = myFavouriteRepository.findByUser_Id(userId);
        if(optional.isEmpty()){
            throw new MyFavouriteErrorException("my favourite not found");
        }
        MyFavouriteEntity myFavouriteEntity = optional.get();
        return myFavouriteConvert.convertToMyFavouriteResponse(myFavouriteEntity);
    }

    @Override
    public String deleteMyFavourites(MyFavouriteDTO myFavouriteDTO) {
        Optional<MyFavouriteEntity> optional = myFavouriteRepository.findByUser_Id(myFavouriteDTO.getUserId());
        if(optional.isEmpty()){
            throw new MyFavouriteErrorException("my favourite hotel not found");
        }
        MyFavouriteEntity myFavouriteEntity = optional.get();
        Optional<HotelEntity> optionalHotel = hotelRepository.findById(myFavouriteDTO.getHotelId());
        if(optionalHotel.isEmpty()){
            throw new HotelErrorException("hotel not found");
        }
        HotelEntity hotelEntity = optionalHotel.get();

        if(!hotelEntity.getMyFavouriteEntities().contains(myFavouriteEntity)){
            throw new MyFavouriteErrorException("hotel not found in my favourite");
        }
        hotelEntity.getMyFavouriteEntities().remove(myFavouriteEntity);
        myFavouriteEntity.getHotels().remove(hotelEntity);

        myFavouriteRepository.save(myFavouriteEntity);
        hotelRepository.save(hotelEntity);

        return "delete my favourite hotel successfully";
    }
}
