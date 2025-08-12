package com.fs.static_content_service.services;

import com.fs.static_content_service.dao.FileRepository;
import com.fs.static_content_service.model.FileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.PathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.*;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final FileRepository fileRepository;

    @Value("${storage.location:/var/data/uploads}")
    private String storageLocation;

    @Value("${storage.thumbnails-folder:thumbnails}")
    private String thumbnailsFolder;

    @Value("${app.public-base-url:}") // e.g. https://cdn.example.com or empty in dev
    private String publicBaseUrl;

    public FileEntity store(MultipartFile file, boolean makePublic) throws IOException {
        Path storageDir = Paths.get(storageLocation);
        Files.createDirectories(storageDir);

        String originalName = file.getOriginalFilename();
        String safeName = UUID.randomUUID() + "_" + (originalName != null ? originalName : "file");
        Path targetPath = storageDir.resolve(safeName);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        FileEntity entity = FileEntity.builder()
                .fileName(originalName)
                .filePath(targetPath.toString())
                .contentType(file.getContentType())
                .size(file.getSize())
                .isPublic(makePublic)
                .build();

        // Persist first to get ID
        entity = fileRepository.save(entity);

        // Generate thumbnail (non-blocking choice: background job is preferable)
        try {
            String thumbRel = createThumbnail(targetPath, safeName);
            entity.setThumbnailPath(thumbRel);
            fileRepository.save(entity);
        } catch (Exception ex) {
            // log and continue — thumbnail failure shouldn't block upload
            // Logger.warn("thumbnail generation failed", ex);
        }

        return entity;
    }

    private String createThumbnail(Path sourcePath, String safeName) throws IOException {
        Path thumbsDir = Paths.get(storageLocation, thumbnailsFolder);
        Files.createDirectories(thumbsDir);
        String thumbName = "thumb_" + safeName + ".jpg";
        Path thumbPath = thumbsDir.resolve(thumbName);

        // use Thumbnailator to create a scaled thumbnail (e.g., 300x300)
        try (InputStream is = Files.newInputStream(sourcePath);
             OutputStream os = Files.newOutputStream(thumbPath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            net.coobird.thumbnailator.Thumbnails.of(is)
                    .size(300, 300)
                    .outputFormat("jpg")
                    .toOutputStream(os);
        }

        // return relative path so moving storage dir is easier
        return Paths.get(thumbnailsFolder, thumbName).toString();
    }

    public PathResource loadAsResource(UUID id, boolean thumbnail) {
        FileEntity file = fileRepository.findById(id).orElseThrow(() -> new RuntimeException("File not found"));
        Path path = thumbnail && file.getThumbnailPath() != null
                ? Paths.get(storageLocation).resolve(file.getThumbnailPath())
                : Paths.get(file.getFilePath());
        if (!Files.exists(path)) throw new RuntimeException("File not found");
        return new org.springframework.core.io.PathResource(path);
    }

    public String publicUrl(FileEntity file) {
        if (publicBaseUrl == null || publicBaseUrl.isBlank()) {
            // fallback to gateway path (assuming gateway forwards /public/files/{id})
            return "/public/files/" + file.getId();
        }
        // If using CDN/origin, construct a full URL
        return publicBaseUrl + "/files/" + file.getId();
    }
}
