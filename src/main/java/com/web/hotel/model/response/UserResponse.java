package com.web.hotel.model.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String name;
    private Long age;
    private String phone;
    private String email;
    private String fileUrl;
    private String roleName;
}
