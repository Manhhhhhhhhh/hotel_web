package com.web.hotel.convert;

import com.web.hotel.model.entity.CommentEntity;
import com.web.hotel.model.entity.FileEntity;
import com.web.hotel.model.entity.UserEntity;
import com.web.hotel.model.response.CommentResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CommentConvert {
    public CommentResponse convertToCommentResponse(CommentEntity commentEntity) {
        UserEntity userEntity = commentEntity.getUser();
        String username = userEntity.getName();
        String content = commentEntity.getContent();
        List<FileEntity> fileEntityList = userEntity.getFileEntities();
        String url = "https://res.cloudinary.com/djuq2enmy/image/upload/" +  fileEntityList.get(0).getFileId();
        return CommentResponse.builder()
                .userName(username)
                .content(content)
                .userFile(url)
                .build();
    }
}
