package com.web.hotel.service;

import com.web.hotel.model.dto.HotelDTO;
import com.web.hotel.model.response.HotelResponse;

import java.util.List;

public interface HotelService {
    public List<HotelResponse> findByRequestParam(HotelDTO hotelDTO);
}
