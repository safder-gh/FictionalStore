package com.fs.static_content_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String filePath;

    private String thumbnailPath; // new

    private String contentType;

    private Long size;

    private Boolean isPublic = false; // default false

    private Instant createdAt = Instant.now();
}
