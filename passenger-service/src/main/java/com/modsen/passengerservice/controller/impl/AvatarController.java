package com.modsen.passengerservice.controller.impl;

import com.modsen.passengerservice.aspect.ValidateAccessToResources;
import com.modsen.passengerservice.controller.general.AvatarOperations;
import com.modsen.passengerservice.dto.AvatarImageDto;
import com.modsen.passengerservice.service.AvatarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/passengers")
public class AvatarController implements AvatarOperations {

    private final AvatarService avatarService;

    @Override
    @PostMapping("/{id}/avatars/upload")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER')")
    @ValidateAccessToResources
    public AvatarImageDto uploadAvatar(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file,
                                       JwtAuthenticationToken jwtAuthenticationToken) {
        return avatarService.uploadAvatar(id, file);
    }

    @Override
    @GetMapping("/{id}/avatars")
    public AvatarImageDto getAvatar(@PathVariable("id") Long id) {
        return avatarService.getAvatar(id);
    }

    @Override
    @DeleteMapping("/{id}/avatars")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER')")
    @ValidateAccessToResources
    public void deleteAvatar(@PathVariable("id") Long id, JwtAuthenticationToken jwtAuthenticationToken) {
        avatarService.deleteAvatar(id);
    }

}
