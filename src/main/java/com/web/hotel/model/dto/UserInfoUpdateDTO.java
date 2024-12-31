package com.web.hotel.model.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoUpdateDTO {
    private Long id;
    private String name;
    private String phone;
    private Long age;
    private MultipartFile file;
}
