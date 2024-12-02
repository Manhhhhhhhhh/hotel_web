package com.web.hotel.service.impl;

import com.web.hotel.convert.HotelConvert;
import com.web.hotel.model.dto.HotelDTO;
import com.web.hotel.model.entity.HotelEntity;
import com.web.hotel.model.response.HotelResponse;
import com.web.hotel.repository.HotelRepository;
import com.web.hotel.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HotelServiceImpl implements HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private HotelConvert hotelConvert;

    @Override
    public List<HotelResponse> findByRequestParam(HotelDTO hotelDTO) {
        List<HotelEntity> hotelEntities = hotelRepository.findByRequestParam(hotelDTO);
        List<HotelResponse> hotelResponses = new ArrayList<>();
        for(HotelEntity hotelEntity : hotelEntities){
            hotelResponses.add(hotelConvert.convertTo(hotelEntity));
        }
        return hotelResponses;
    }
}
