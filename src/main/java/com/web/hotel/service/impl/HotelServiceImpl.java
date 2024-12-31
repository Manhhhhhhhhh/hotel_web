package com.web.hotel.service.impl;

import com.web.hotel.convert.HotelConvert;
import com.web.hotel.exception.HotelErrorException;
import com.web.hotel.exception.UserErrorException;
import com.web.hotel.model.dto.HotelDTO;
import com.web.hotel.model.entity.FileEntity;
import com.web.hotel.model.entity.HotelEntity;
import com.web.hotel.model.entity.MyFavouriteEntity;
import com.web.hotel.model.entity.UserEntity;
import com.web.hotel.model.response.HotelResponse;
import com.web.hotel.repository.HotelRepository;
import com.web.hotel.repository.MyFavouriteRepository;
import com.web.hotel.repository.UserRepository;
import com.web.hotel.service.CloudinaryService;
import com.web.hotel.service.HotelService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class HotelServiceImpl implements HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private HotelConvert hotelConvert;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MyFavouriteRepository myFavouriteRepository;


    @Autowired
    private UserRepository userRepository;

    @Override
    public List<HotelResponse> findByRequestParam(HotelDTO hotelDTO) {
        List<HotelEntity> hotelEntities = hotelRepository.findByRequestParam(hotelDTO);
        List<HotelResponse> hotelResponses = new ArrayList<>();
        for(HotelEntity hotelEntity : hotelEntities){
            hotelResponses.add(hotelConvert.convertToHotelResponse(hotelEntity));
        }
        return hotelResponses;
    }

    @Override
    public String create(HotelDTO hotelDTO, Long useId) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(useId);
        if(optionalUserEntity.isEmpty()){
            throw new UserErrorException("user not found");
        }
        UserEntity userEntity = optionalUserEntity.get();
        if(userEntity.getRole().getId() == 2){
            throw new UserErrorException("user not authorize");
        }

        List<MyFavouriteEntity> myFavouriteEntities =  new ArrayList<>();
        MyFavouriteEntity myFavouriteEntity =  MyFavouriteEntity.builder()
                .hotels(new ArrayList<>())
                .build();
        myFavouriteEntities.add(myFavouriteEntity);


        HotelEntity hotel = HotelEntity.builder()
                .address(hotelDTO.getAddress())
                .name(hotelDTO.getName())
                .rate(hotelDTO.getRate())
                .description(hotelDTO.getDescription())
                .price(hotelDTO.getPrice())
                .numberOfRoom(hotelDTO.getNumberOfRoom())
                .detail(hotelDTO.getDetail())
                .category(hotelDTO.getCategories().get(0))
                .dealEntities(new ArrayList<>())
                .fileEntities(new ArrayList<>())
                .users(new ArrayList<>())
                .myFavouriteEntities(myFavouriteEntities)
                .commentEntities(new ArrayList<>())
                .build();
        if(hotelDTO.getFiles() != null){
            for(MultipartFile multipartFile : hotelDTO.getFiles()) {
                Map res = cloudinaryService.upload(multipartFile);
                FileEntity fileEntity = FileEntity.builder()
                        .fileId((String) res.get("public_id"))
                        .fileName((String) res.get("name"))
                        .fileName((String) res.get("original_filename"))
                        .build();
                fileEntity.setHotel(hotel);
                hotel.getFileEntities().add(fileEntity);
            }
        }
        userEntity.getHotelEntities().add(hotel);
        hotel.getUsers().add(userEntity);

        myFavouriteRepository.save(myFavouriteEntity);
        userRepository.save(userEntity);
        hotelRepository.save(hotel);
        return "create successfully";
    }

    @Override
    public String update(HotelDTO hotelDTO) {
        Optional<HotelEntity> optional = hotelRepository.findById(hotelDTO.getId());
        if(optional.isEmpty()){
            return "hotel not found";
        }
        HotelEntity hotel = optional.get();
        modelMapper.map(hotelDTO, hotel);
        if(hotelDTO.getFiles() != null){
            for(MultipartFile multipartFile : hotelDTO.getFiles()) {
                Map res = cloudinaryService.upload(multipartFile);
                FileEntity fileEntity = FileEntity.builder()
                        .fileId((String) res.get("public_id"))
                        .fileName((String) res.get("name"))
                        .fileName((String) res.get("original_filename"))
                        .build();
                fileEntity.setHotel(hotel);
                hotel.getFileEntities().add(fileEntity);
            }
        }
        hotelRepository.save(hotel);
        return "update successfully";
    }

    @Override
    public String delete(Long hotelId) {
        Optional<HotelEntity> optional = hotelRepository.findById(hotelId);
        if(optional.isEmpty()){
            throw new HotelErrorException("hotel not found");
        }
        HotelEntity hotel = optional.get();
        List<UserEntity> userEntities = userRepository.findAllByHotelEntitiesContaining(hotel);
        List<MyFavouriteEntity> myFavouriteEntities = myFavouriteRepository.findAllByHotelsContaining(hotel);
        for(UserEntity userEntity : userEntities){
            userEntity.getHotelEntities().remove(hotel);
        }
        for(MyFavouriteEntity myFavouriteEntity : myFavouriteEntities){
            myFavouriteEntity.getHotels().remove(hotel);
        }
        hotelRepository.deleteById(hotelId);
        return "delete successfully";
    }

    @Override
    public HotelResponse findById(Long id) {
        Optional<HotelEntity> optional = hotelRepository.findById(id);
        if(optional.isEmpty()){
            return null;
        }
        HotelEntity hotel = optional.get();
        return hotelConvert.convertToHotelResponse(hotel);
    }

    @Override
    public List<HotelResponse> getHotelsManagement(Long userId) {
        Optional<UserEntity> optional = userRepository.findById(userId);
        if(optional.isEmpty()){
            throw new UserErrorException("user not found");
        }
        UserEntity user = optional.get();
        if(user.getRole().getId() == 2){
            throw new UserErrorException("user not authorize");
        }
        return user.getHotelEntities().stream().
                map(it -> hotelConvert.convertToHotelResponse(it))
                .toList();

    }


}
