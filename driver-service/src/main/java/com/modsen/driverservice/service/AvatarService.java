package com.modsen.driverservice.service;

import com.modsen.driverservice.dto.AvatarImageDto;
import org.springframework.web.multipart.MultipartFile;

public interface AvatarService {

    AvatarImageDto uploadAvatar(Long id, MultipartFile file);

    AvatarImageDto getAvatar(Long id);

    void deleteAvatar(Long id);

}
