package com.web.hotel.convert;

import com.web.hotel.model.entity.HotelEntity;
import com.web.hotel.model.entity.MyFavouriteEntity;
import com.web.hotel.model.response.HotelResponse;
import com.web.hotel.model.response.MyFavouriteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class MyFavouriteConvert {

    @Autowired
    private HotelConvert hotelConvert;

    public MyFavouriteResponse convertToMyFavouriteResponse(MyFavouriteEntity myFavouriteEntity) {

        List<HotelEntity> hotelEntities = myFavouriteEntity.getHotels();
        List<HotelResponse> hotelResponses = new ArrayList<>();
        for(HotelEntity hotelEntity : hotelEntities) {
            hotelResponses.add(hotelConvert.convertToHotelResponse(hotelEntity));
        }
        return   MyFavouriteResponse.builder()
                .hotels(hotelResponses)
                .id(myFavouriteEntity.getId())
                .build();
    }
}
