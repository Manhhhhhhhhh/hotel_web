package com.web.hotel.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hotel")
public class HotelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "price")
    private Double price;

    @Column(name = "rate")
    private Double rate;

    @Column(name = "category")
    private String category;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "detail", columnDefinition = "TEXT")
    private String detail;

    @Column(name = "number_of_room")
    private Long numberOfRoom;

    @OneToMany(mappedBy = "hotel", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<CommentEntity> commentEntities ;

    @OneToMany(mappedBy = "hotel", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<FileEntity> fileEntities;

    @OneToMany(mappedBy = "hotel", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<DealEntity> dealEntities ;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "hotelfavourite",
                joinColumns = @JoinColumn(name = "hotel_id", nullable = false),
                inverseJoinColumns = @JoinColumn(name = "myfavourite_id", nullable = false))
    private List<MyFavouriteEntity> myFavouriteEntities;

    @ManyToMany(mappedBy = "hotelEntities")
    private List<UserEntity> users;

}
