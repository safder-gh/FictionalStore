package com.fs.static_content_service.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fs.static_content_service.model.*;
import java.util.UUID;

public interface FileRepository extends JpaRepository<FileEntity, UUID> {
}
