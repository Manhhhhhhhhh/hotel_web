package com.web.hotel.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface CloudinaryService {
    Map upload(MultipartFile file);
}
