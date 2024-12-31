package com.web.hotel.service.impl;

import com.web.hotel.convert.DealConvert;
import com.web.hotel.exception.DealErrorException;
import com.web.hotel.exception.HotelErrorException;
import com.web.hotel.model.dto.DealDTO;
import com.web.hotel.model.entity.DealEntity;
import com.web.hotel.model.entity.HotelEntity;
import com.web.hotel.model.response.DealResponse;
import com.web.hotel.repository.DealRepository;
import com.web.hotel.repository.HotelRepository;
import com.web.hotel.service.DealService;
import com.web.hotel.service.HotelService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DealServiceImpl implements DealService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private DealConvert dealConvert;
    @Autowired
    private DealRepository dealRepository;

    @Override
    public List<DealResponse> getDeal(Long hotelId) {
        Optional<HotelEntity> optionalHotel = hotelRepository.findById(hotelId);
        if(optionalHotel.isEmpty()) {
            throw new HotelErrorException("hotel not found");
        }
        HotelEntity hotel = optionalHotel.get();
        return hotel.getDealEntities().stream()
                .map(it -> dealConvert.convertToDealResponse(it))
                .toList();
    }

    @Override
    public String deal(DealDTO dealDTO) {
        Long dealId = dealDTO.getId();
        Optional<DealEntity> optionalDeal = dealRepository.findById(dealId);
        if(optionalDeal.isEmpty()) {
            throw new DealErrorException("deal not found");
        }
        DealEntity dealEntity = optionalDeal.get();
        String status = dealDTO.getStatus();
        dealEntity.setStatus(status);
        dealRepository.save(dealEntity);
        return "success";
    }
}
