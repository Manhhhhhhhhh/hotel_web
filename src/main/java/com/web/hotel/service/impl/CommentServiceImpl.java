package com.web.hotel.service.impl;

import com.web.hotel.convert.CommentConvert;
import com.web.hotel.model.entity.CommentEntity;
import com.web.hotel.model.response.CommentResponse;
import com.web.hotel.repository.CommentRepository;
import com.web.hotel.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentConvert commentConvert;

    @Override
    public List<CommentResponse> findByHotelId(Long hotelId) {
        try{
            List<CommentEntity> commentEntities = commentRepository.findByHotel_Id(hotelId);
            List<CommentResponse> commentResponses = new ArrayList<>();
            for(CommentEntity commentEntity : commentEntities){
                commentResponses.add(commentConvert.convertToCommentResponse(commentEntity));
            }
            return commentResponses;
        } catch (Exception e) {
            return null;
        }
    }
}
