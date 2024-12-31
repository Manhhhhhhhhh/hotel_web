package com.web.hotel.model.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Long id;
    private Long userId;
    private Long hotelId;
    private String content;
}
