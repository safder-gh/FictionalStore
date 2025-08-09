package com.fs.product_service.dao;

import com.fs.product_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    // Find product by SKU
    Optional<Product> findBySku(String sku);

    // Check if a product exists by SKU
    boolean existsBySku(String sku);
}