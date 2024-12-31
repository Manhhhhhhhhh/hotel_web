package com.web.hotel.model.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyFavouriteDTO {
    private Long id;
    private Long hotelId;
    private Long userId;
}
