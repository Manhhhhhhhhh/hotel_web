package com.web.hotel.repository;

import com.web.hotel.model.entity.HotelEntity;
import com.web.hotel.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    List<UserEntity> findAllByHotelEntitiesContaining(HotelEntity hotel);
}
