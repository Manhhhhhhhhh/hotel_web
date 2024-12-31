package com.web.hotel.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "age")
    private Long age;

    @Column(name = "enable")
    private boolean enable = false;

    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "verification_code_expire")
    private Date verificationCodeExpire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private RoleEntity role;

    @Column(name = "resetCode")
    private String resetCode;

    @Column(name = "resetCodeExpiration")
    private Date resetCodeExpiration;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "managerhotel",
                joinColumns = @JoinColumn(name = "user_id", nullable = false),
                inverseJoinColumns = @JoinColumn(name = "hotel_id", nullable = false))
    private List<HotelEntity> hotelEntities;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.PERSIST},orphanRemoval = true)
    private List<MyFavouriteEntity> myFavouriteEntities;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.PERSIST},orphanRemoval = true)
    private List<CommentEntity> commentEntities;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.PERSIST},orphanRemoval = true)
    private List<FileEntity> fileEntities;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.PERSIST},orphanRemoval = true)
    private List<DealEntity> dealEntities;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.PERSIST},orphanRemoval = true)
    private List<RefreshTokenEntity> refreshTokenEntities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        return authorities;
    }
}
