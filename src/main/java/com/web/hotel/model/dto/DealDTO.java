package com.web.hotel.model.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DealDTO {
    // form chinh
    private Long id;
    private Long numberOfRoom;
    private String start;
    private String end;
    private String status;
    // fe tu gan hai gia tri nay
    private Long userId;
    private Long hotelId;
}
