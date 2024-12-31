package com.web.hotel.model.response;

import com.web.hotel.model.dto.FileDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotelResponse {
    private Long id;
    private String name;
    private String address;
    private Double price;
    private Double rate;
    private List<String> categories;
    private String description;
    private String detail;
    private List<String> fileDTOs;
    private Long numberOfRoom;
}
