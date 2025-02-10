package com.modsen.driverservice.controller.impl;

import com.modsen.driverservice.service.AvatarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/drivers")
public class AvatarController {

    private final AvatarService avatarService;

    @PostMapping("/{id}/avatars/upload")
    public String uploadAvatar(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file) {
        return avatarService.uploadFile(id, file);
    }

}
