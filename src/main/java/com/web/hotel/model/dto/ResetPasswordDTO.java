package com.web.hotel.model.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordDTO {
    private String email;
    private String newPassword;
    private String resetCode;
}
