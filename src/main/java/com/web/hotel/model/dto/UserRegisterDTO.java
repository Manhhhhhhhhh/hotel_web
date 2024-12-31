package com.web.hotel.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDTO {
    @JsonProperty("name")
    @NotBlank(message = "name required")
    private String name;

    @JsonProperty("username")
    @NotBlank(message = "username required")
    private String username;

    @JsonProperty("password")
    @NotBlank(message = "password required")
    private String password;

    @JsonProperty("retype")
    @NotBlank(message = "retype password required")
    private String retype;

    @JsonProperty("age")
    private Long age;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("email")
    @NotBlank(message = "email required")
    private String email;

    @JsonProperty("role")
    @NotNull(message = "role required")
    private Long roleId;
}
