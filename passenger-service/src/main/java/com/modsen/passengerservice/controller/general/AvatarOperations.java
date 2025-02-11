package com.modsen.passengerservice.controller.general;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface AvatarOperations {

    ResponseEntity<InputStreamResource> uploadAvatar(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file,
                                                     JwtAuthenticationToken jwtAuthenticationToken);

    ResponseEntity<InputStreamResource> getAvatar(@PathVariable("id") Long id);

    void deleteAvatar(@PathVariable("id") Long id, JwtAuthenticationToken jwtAuthenticationToken);

}
