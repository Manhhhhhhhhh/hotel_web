package com.web.hotel.service;

import com.web.hotel.model.response.CommentResponse;

import java.util.List;

public interface CommentService {
    List<CommentResponse> findByHotelId(Long hotelId);
}
