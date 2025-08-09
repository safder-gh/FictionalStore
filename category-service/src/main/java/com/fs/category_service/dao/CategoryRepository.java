package com.fs.category_service.dao;

import com.fs.category_service.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    // Find category by slug
    Optional<Category> findBySlug(String slug);

    // Check if a category exists by slug
    boolean existsBySlug(String slug);
}