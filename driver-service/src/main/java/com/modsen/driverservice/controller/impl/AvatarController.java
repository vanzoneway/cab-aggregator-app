package com.modsen.driverservice.controller.impl;

import com.modsen.driverservice.aspect.ValidateAccessToResources;
import com.modsen.driverservice.constants.AvatarServiceLiteralConstants;
import com.modsen.driverservice.controller.AvatarOperations;
import com.modsen.driverservice.dto.MinioFileInformation;
import com.modsen.driverservice.service.AvatarService;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/v1/drivers")
public class AvatarController implements AvatarOperations {

    private final AvatarService avatarService;

    @Override
    @PostMapping("/{id}/avatars/upload")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER')")
    @ValidateAccessToResources
    public ResponseEntity<InputStreamResource> uploadAvatar(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file,
                                       JwtAuthenticationToken jwtAuthenticationToken) {

        MinioFileInformation minioFileInformation = avatarService.uploadAvatar(id, file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.CONTENT_DISPOSITION, AvatarServiceLiteralConstants.CONTENT_DISPOSITION_VALUE)
                .contentType(minioFileInformation.mediaType())
                .body(new InputStreamResource(minioFileInformation.is()));
    }

    @Override
    @GetMapping("/{id}/avatars")
    public ResponseEntity<InputStreamResource> getAvatar(@PathVariable("id") Long id) {
        MinioFileInformation minioFileInformation = avatarService.getAvatar(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, AvatarServiceLiteralConstants.CONTENT_DISPOSITION_VALUE)
                .contentType(minioFileInformation.mediaType())
                .body(new InputStreamResource(minioFileInformation.is()));
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
