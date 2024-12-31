package com.web.hotel.model.response;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DealResponse {
    private Long id;
    private Long numberOfRoomBooking;
    private String status;
    private String start;
    private String end;
    private HotelResponse hotel;
    private UserResponse user;
}
