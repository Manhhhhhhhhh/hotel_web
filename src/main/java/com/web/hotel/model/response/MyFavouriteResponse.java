package com.web.hotel.model.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyFavouriteResponse {
    private Long id;
    private List<HotelResponse> hotels;
}
