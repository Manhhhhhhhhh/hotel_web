package com.web.hotel.convert;

import com.web.hotel.model.entity.DealEntity;
import com.web.hotel.model.entity.HotelEntity;
import com.web.hotel.model.response.DealResponse;
import com.web.hotel.model.response.HotelResponse;
import com.web.hotel.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class DealConvert {

    @Autowired
    private HotelConvert hotelConvert;

    @Autowired
    private UserConvert userConvert;

    public DealResponse convertToDealResponse(DealEntity deal){
        HotelEntity hotelEntity = deal.getHotel();
        HotelResponse hotelResponse = hotelConvert.convertToHotelResponse(hotelEntity);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        DealResponse dealResponse = DealResponse.builder()
                .id(deal.getId())
                .status(deal.getStatus())
                .numberOfRoomBooking(deal.getNumberOfRoom())
                .hotel(hotelResponse)
                .start(sdf.format(deal.getStart()))
                .end(sdf.format(deal.getEnd()))
                .user(userConvert.convertTo(deal.getUser()))
                .build();
        return  dealResponse;
    }
}
