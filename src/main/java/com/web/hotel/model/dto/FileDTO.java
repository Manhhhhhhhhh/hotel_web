package com.web.hotel.model.dto;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileDTO {
    private String fileName;
    private String fileId;
    private String fileUrl;
}
