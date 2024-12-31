package com.web.hotel.convert;

import com.web.hotel.model.dto.UserInfoUpdateDTO;
import com.web.hotel.model.entity.FileEntity;
import com.web.hotel.model.entity.RoleEntity;
import com.web.hotel.model.entity.UserEntity;
import com.web.hotel.model.response.UserResponse;
import com.web.hotel.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class UserConvert {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RoleRepository roleRepository;

    public UserResponse convertTo(UserEntity userEntity) {
        UserResponse userResponse = modelMapper.map(userEntity, UserResponse.class);
        // role
        userResponse.setRoleName(userEntity.getRole().getRoleName());
        // file url
        List<FileEntity> files = userEntity.getFileEntities();
        if(files != null && !files.isEmpty()) {
            FileEntity file = files.get(0);
            userResponse.setFileUrl("https://res.cloudinary.com/djuq2enmy/image/upload/" + file.getFileId());
        }
        return userResponse;
    }
}
