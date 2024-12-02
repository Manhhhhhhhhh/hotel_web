package com.web.hotel.repository;


import com.web.hotel.model.entity.RefreshTokenEntity;
import com.web.hotel.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    void deleteByUser(UserEntity user);
}
