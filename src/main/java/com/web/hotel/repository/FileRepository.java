package com.web.hotel.repository;

import com.web.hotel.model.entity.FileEntity;
import com.web.hotel.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
    void deleteByUser(UserEntity user);
}
