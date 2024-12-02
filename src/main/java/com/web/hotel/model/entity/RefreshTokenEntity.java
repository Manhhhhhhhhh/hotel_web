package com.web.hotel.model.entity;

import jakarta.persistence.*;
import lombok.*;


@Setter
@Getter
@Builder
@Table(name = "refreshtoken")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "refresh_token")
    private String refreshToken;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;
}
