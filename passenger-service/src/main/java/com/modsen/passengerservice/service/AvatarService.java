package com.modsen.passengerservice.service;

import com.modsen.passengerservice.dto.MinioFileInformation;
import org.springframework.web.multipart.MultipartFile;

public interface AvatarService {

    MinioFileInformation uploadAvatar(Long id, MultipartFile file);

    MinioFileInformation getAvatar(Long id);

    void deleteAvatar(Long id);

}
