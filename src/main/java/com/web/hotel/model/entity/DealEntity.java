package com.web.hotel.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "deal")
public class DealEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number_of_room")
    private Long numberOfRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    private HotelEntity hotel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "status")
    private String status;

    @Column(name = "start")
    private Date start;

    @Column(name = "end")
    private Date end;
}
