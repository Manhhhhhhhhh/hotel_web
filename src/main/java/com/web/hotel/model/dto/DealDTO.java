package com.web.hotel.model.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DealDTO {
    private Long numberOfRoom;
    private Long userId;
    private Long hotelId;
}
