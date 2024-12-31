package com.web.hotel.model.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenDTO {
    private String refreshToken;
    private String username;
}
