package com.web.hotel.repository;

import com.web.hotel.model.entity.HotelEntity;
import com.web.hotel.repository.custom.HotelRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<HotelEntity, Long>, HotelRepositoryCustom {
}
