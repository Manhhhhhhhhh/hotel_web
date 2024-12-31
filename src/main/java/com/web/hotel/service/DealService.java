package com.web.hotel.service;

import com.web.hotel.model.dto.DealDTO;
import com.web.hotel.model.response.DealResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface DealService {
    List<DealResponse> getDeal(Long hotelId);
    String deal(DealDTO dealDTO);
}
