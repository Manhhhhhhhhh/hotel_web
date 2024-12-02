package com.web.hotel.repository.custom;

import com.web.hotel.model.dto.HotelDTO;
import com.web.hotel.model.entity.HotelEntity;

import java.util.List;

public interface HotelRepositoryCustom {
    List<HotelEntity> findByRequestParam(HotelDTO hotelDTO);
}
