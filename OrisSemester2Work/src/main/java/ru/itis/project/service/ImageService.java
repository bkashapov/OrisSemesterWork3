package ru.itis.project.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class ImageService {

    public static String DEFAULT_IMAGE_FILENAME = "default.png";

    public String saveMultipartFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Файл должен быть изображением");
        }

        String extension = switch (contentType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/gif" -> ".gif";
            default -> throw new IllegalArgumentException("Неподдерживаемый тип изображения: " + contentType);
        };

        String filename = UUID.randomUUID() + extension;

        Path directory = Path.of("uploads/images");
        Path savePath = directory.resolve(filename);

        try {
            Files.createDirectories(directory);
            file.transferTo(savePath);
            return savePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении изображения", e);
        }
    }
}
