package com.fs.static_content_service.controller;

import com.fs.static_content_service.model.FileEntity;
import com.fs.static_content_service.services.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService storageService;

    // Admin / authenticated upload endpoint (keeps metadata flags, etc.)
    @PostMapping
    public ResponseEntity<FileEntity> upload(@RequestParam("file") MultipartFile file,
                                             @RequestParam(value = "public", defaultValue = "false") boolean isPublic) throws IOException {
        FileEntity saved = storageService.store(file, isPublic);
        return ResponseEntity.ok(saved);
    }

    // Download (internal) — can be protected behind gateway auth
    @GetMapping("/internal/{id}")
    public ResponseEntity<Resource> downloadInternal(@PathVariable UUID id) {
        Resource res = storageService.loadAsResource(id, false);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(detectContentType(res, "application/octet-stream")))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + suggestFileName(id) + "\"")
                .body(res);
    }

    // PUBLIC endpoint served by API Gateway (no auth required)
    @GetMapping("/public/{id}")
    public ResponseEntity<Resource> downloadPublic(@PathVariable UUID id) {
        Resource res = storageService.loadAsResource(id, false);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(detectContentType(res, "application/octet-stream")))
                .body(res);
    }

    // Public thumbnail
    @GetMapping("/public/{id}/thumbnail")
    public ResponseEntity<Resource> thumbnail(@PathVariable UUID id) {
        Resource res = storageService.loadAsResource(id, true);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(res);
    }

    // helper to read content type safely
    private String detectContentType(Resource resource, String fallback) {
        try {
            return Files.probeContentType(resource.getFile().toPath());
        } catch (Exception e) {
            return fallback;
        }
    }

    private String suggestFileName(UUID id) {
        return id.toString();
    }
}
