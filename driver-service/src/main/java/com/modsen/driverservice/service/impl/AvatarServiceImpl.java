package com.modsen.driverservice.service.impl;

import com.modsen.driverservice.constants.AppConstants;
import com.modsen.driverservice.service.AvatarService;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import io.minio.PutObjectArgs;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AvatarServiceImpl implements AvatarService {

    //TODO 1) Создать следующие Exception: большой размер файла, неподдерживаемый формат, ошибка загрузки файла в целом
    //TODO 2) Подготовить все ошибки для MessageSource
    //TODO 3) Все литералы переместить в константы
    //TODO 4) Сделать ImageDto которая имеет url, filename, contentType
    //TODO 5) Сделать методы для получения ссылки на изображение с MinIO для конкретного водителя
    //TODO 6) Сделать ендпоинт для удаления определенной картинки
    //TODO 7) Настроить Security под это все дело.
    private final MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String bucketName;

    public String uploadFile(Long id, MultipartFile file) {
        String presignedUrl = "";

        // Ограничения
        final long MAX_SIZE = 10 * 1024 * 1024; // 10 MB
        final List<String> ALLOWED_TYPES = Arrays.asList("image/jpeg", "image/png");

        // Проверка размера файла
        if (file.getSize() > MAX_SIZE) {
            throw new RuntimeException("Файл превышает максимальный размер в 10 MB");
        }

        // Проверка формата файла
        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new RuntimeException("Недопустимый формат файла. Допустимые форматы: JPEG, PNG");
        }

        try {
            InputStream inputStream = file.getInputStream();

            // Извлечение расширения файла
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : ""; // Получаем расширение файла

            // Формируем новое имя файла
            String newFileName = "driver-avatar-" + id + extension;

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(newFileName) // Используем новое имя файла
                            .contentType(file.getContentType()) // Используем тип контента файла
                            .stream(inputStream, inputStream.available(), -1)
                            .headers(Map.of("Content-Disposition", "inline"))
                            .build());

            presignedUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucketName)
                            .method(Method.GET)
                            .object(newFileName) // Используем новое имя файла
                            .expiry(60 * 60 * 24)
                            .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(AppConstants.INTERNAL_SERVER_ERROR);
        }

        return presignedUrl;
    }

}
