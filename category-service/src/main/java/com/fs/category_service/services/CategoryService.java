package com.fs.category_service.services;

import com.fs.category_service.dao.CategoryRepository;
import com.fs.category_service.model.Category;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    public Category createCategory(Category category) {
        if (categoryRepository.existsBySlug(category.getSlug())) {
            throw new IllegalArgumentException("Category with slug '" + category.getSlug() + "' already exists");
        }
        return categoryRepository.save(category);
    }

    public Category getCategory(UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + categoryId));
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category updateCategory(UUID categoryId, Category updatedCategory) {
        Category existing = getCategory(categoryId);
        existing.setName(updatedCategory.getName());
        existing.setSlug(updatedCategory.getSlug());
        existing.setDescription(updatedCategory.getDescription());
        existing.setParent(updatedCategory.getParent());
        return categoryRepository.save(existing);
    }

    public void deleteCategory(UUID categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new EntityNotFoundException("Category not found with ID: " + categoryId);
        }
        categoryRepository.deleteById(categoryId);
    }

    public List<Category> getSubcategories(UUID parentId) {
        return categoryRepository.findAll().stream()
                .filter(cat -> cat.getParent() != null && cat.getParent().getCategoryId().equals(parentId))
                .toList();
    }
}