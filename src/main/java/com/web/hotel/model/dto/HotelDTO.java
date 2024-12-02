package com.web.hotel.model.dto;


import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotelDTO {
    private String name;
    private String address;
    private Double price;
    private Double rate;
    private List<String> categories;
    private String description;
    private String detail;
    private List<FileDTO> files;
    private Long numberOfRoom;
}
