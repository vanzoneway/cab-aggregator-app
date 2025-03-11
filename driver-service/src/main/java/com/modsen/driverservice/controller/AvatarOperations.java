package com.modsen.driverservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Validated
@Tag(name = "avatar-operations", description = """
        The endpoints contained here are intended for operations related to driver avatars. For example: uploading an avatar,
        retrieving an avatar, and deleting an avatar.

        It is important to note that JWT authorization is used here: only ROLE_ADMIN or ROLE_DRIVER can perform certain actions.
        """)
@SecurityRequirement(name = "bearerAuth")
public interface AvatarOperations {

    @Operation(summary = "Uploads a new avatar for a driver",
            description = """
                    Uploads a new avatar for a specific driver. Only accessible by ADMIN or the DRIVER themselves.

                    Required fields:
                    - **file**: The image file to be uploaded as an avatar (must be one of the allowed types: JPEG, PNG, etc.)

                    Validation rules:
                    - The file must not be empty.
                    - The file type must be one of the allowed types (JPEG, PNG, etc.).

                    Example request:
                    POST /api/v1/drivers/1/avatars/upload
                    - Headers: Authorization: Bearer <JWT_TOKEN>
                    - Body: Form-data with key "file" and the image file as value.

                    Example response:
                    - Status Code: 201 (Created)
                    - Headers: Content-Disposition: attachment; filename=<avatar_name>
                    - Body: The uploaded image file as a stream.
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Avatar uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid file type or empty file"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden, only ADMIN or DRIVER can access this endpoint"),
            @ApiResponse(responseCode = "404", description = "Driver not found")
    })
    ResponseEntity<InputStreamResource> uploadAvatar(
            @PathVariable("id") Long id,
            @RequestParam("file") MultipartFile file,
            JwtAuthenticationToken jwtAuthenticationToken);

    @Operation(summary = "Retrieves a driver's avatar by ID",
            description = """
                    Retrieves a driver's avatar by their ID.

                    Example request:
                    GET /api/v1/drivers/1/avatars

                    Example response:
                    - Status Code: 200 (OK)
                    - Headers: Content-Disposition: attachment; filename=<avatar_name>
                    - Body: The image file as a stream.
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved avatar"),
            @ApiResponse(responseCode = "404", description = "Avatar or driver not found")
    })
    ResponseEntity<InputStreamResource> getAvatar(@PathVariable("id") Long id);

    @Operation(summary = "Deletes a driver's avatar by ID",
            description = """
                    Deletes a driver's avatar by their ID. Only accessible by ADMIN or the DRIVER themselves.

                    Example request:
                    DELETE /api/v1/drivers/1/avatars

                    Example response:
                    - Status Code: 204 (No Content)
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Avatar deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden, only ADMIN or DRIVER can access this endpoint"),
            @ApiResponse(responseCode = "404", description = "Avatar or driver not found")
    })
    void deleteAvatar(@PathVariable("id") Long id, JwtAuthenticationToken jwtAuthenticationToken);

}