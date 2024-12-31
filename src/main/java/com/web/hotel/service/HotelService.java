package com.web.hotel.service;

import com.web.hotel.model.dto.HotelDTO;
import com.web.hotel.model.response.Hotel;
import com.web.hotel.model.response.HotelResponse;

import java.util.List;

public interface HotelService {
    public List<HotelResponse> findByRequestParam(HotelDTO hotelDTO);
    public String create(HotelDTO hotelDTO, Long userId);
    public String update(HotelDTO hotelDTO);
    public String delete(Long hotelId);
    public HotelResponse findById(Long id);
    public List<HotelResponse> getHotelsManagement(Long userId);
}
