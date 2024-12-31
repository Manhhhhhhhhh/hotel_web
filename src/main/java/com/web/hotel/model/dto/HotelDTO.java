package com.web.hotel.model.dto;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotelDTO {
    private Long id;
    private String name;
    private String address;
    private Double price;
    private Double rate;
    private List<String> categories;
    private String description;
    private String detail;
    private List<MultipartFile> files;
    private Long numberOfRoom;
}
