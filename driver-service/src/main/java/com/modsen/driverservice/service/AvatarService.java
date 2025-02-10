package com.modsen.driverservice.service;

import org.springframework.web.multipart.MultipartFile;

public interface AvatarService {

    String uploadFile(Long id, MultipartFile file);
}
