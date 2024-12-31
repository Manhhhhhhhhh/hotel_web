package com.web.hotel.repository;

import com.web.hotel.model.entity.DealEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DealRepository extends JpaRepository<DealEntity, Long> {
    List<DealEntity> findByUser_Id(Long id);
}
