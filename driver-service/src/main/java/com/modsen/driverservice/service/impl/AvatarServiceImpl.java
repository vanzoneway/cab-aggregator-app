package com.modsen.driverservice.service.impl;

import com.modsen.driverservice.constants.AppConstants;
import com.modsen.driverservice.constants.AvatarServiceLiteralConstants;
import com.modsen.driverservice.dto.MinioFileInformation;
import com.modsen.driverservice.exception.avatar.NoSuchAvatarException;
import com.modsen.driverservice.exception.avatar.UnsupportedFileTypeException;
import com.modsen.driverservice.exception.driver.DriverNotFoundException;
import com.modsen.driverservice.repository.DriverRepository;
import com.modsen.driverservice.service.AvatarService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.errors.ErrorResponseException;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import io.minio.PutObjectArgs;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AvatarServiceImpl implements AvatarService {

    private final MinioClient minioClient;

    private final MessageSource messageSource;

    private final DriverRepository driverRepository;

    @Value("${minio.bucket.name}")
    private String bucketName;

    @Value("${minio.file.types}")
    private List<String> allowedTypes;

    @Override
    @SneakyThrows
    public MinioFileInformation uploadAvatar(Long id, MultipartFile file) {
        validateDriverExistence(id);
        validateFileType(file);
        InputStream inputStream = file.getInputStream();
        String newFileName = AvatarServiceLiteralConstants.BASE_DRIVER_AVATAR_NAME + id;
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(newFileName)
                        .contentType(file.getContentType())
                        .stream(inputStream, inputStream.available(), -1)
                        .headers(Map.of(HttpHeaders.CONTENT_DISPOSITION,
                                AvatarServiceLiteralConstants.CONTENT_DISPOSITION_VALUE))
                        .build());
        return getImageAsMinioFileInformation(newFileName);
    }

    @Override
    public MinioFileInformation getAvatar(Long id) {
        validateDriverExistence(id);
        return getImageAsMinioFileInformation(AvatarServiceLiteralConstants.BASE_DRIVER_AVATAR_NAME + id);
    }

    @Override
    @SneakyThrows
    public void deleteAvatar(Long id) {
        validateDriverExistence(id);
        String objectName = AvatarServiceLiteralConstants.BASE_DRIVER_AVATAR_NAME + id;
        try {
            minioClient.statObject(StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());
        } catch (ErrorResponseException e) {
            throw new NoSuchAvatarException(messageSource.getMessage(
                    AppConstants.NO_SUCH_AVATAR_MESSAGE_KEY,
                    new Object[]{objectName},
                    LocaleContextHolder.getLocale()));
        }
    }

    private void validateFileType(MultipartFile file) {
        if (!allowedTypes.contains(file.getContentType())) {
            throw new UnsupportedFileTypeException(messageSource.getMessage(
                    AppConstants.UNSUPPORTED_FILE_TYPE_MESSAGE_KEY,
                    new Object[]{file.getOriginalFilename()},
                    LocaleContextHolder.getLocale()));
        }
    }

    @SneakyThrows
    private MinioFileInformation getImageAsMinioFileInformation(String bucketObjectName) {
        try {
            StatObjectResponse statObjectResponse = minioClient.statObject(StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(bucketObjectName)
                    .build());
            InputStream is = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(bucketObjectName)
                            .build());
            return MinioFileInformation.builder()
                    .withIs(is)
                    .withMediaType(MediaType.valueOf(statObjectResponse.contentType()))
                    .build();
        } catch (ErrorResponseException e) {
            throw new NoSuchAvatarException(messageSource.getMessage(
                    AppConstants.NO_SUCH_AVATAR_MESSAGE_KEY,
                    new Object[]{bucketObjectName},
                    LocaleContextHolder.getLocale()));
        }
    }

    private void validateDriverExistence(Long driverId) {
        if (!driverRepository.existsById(driverId)) {
            throw new DriverNotFoundException(messageSource.getMessage(
                    AppConstants.DRIVER_NOT_FOUND_MESSAGE_KEY,
                    new Object[]{driverId},
                    LocaleContextHolder.getLocale()));
        }
    }

}
