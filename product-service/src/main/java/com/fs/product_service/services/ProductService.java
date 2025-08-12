package com.fs.product_service.services;

import com.fs.product_service.dao.ProductImageRepository;
import com.fs.product_service.dao.ProductRepository;
import com.fs.product_service.interfaces.ContentFeignClient;
import com.fs.product_service.model.Product;
import com.fs.product_service.model.ProductImage;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductImageRepository imageRepository;
    @Autowired
    ContentFeignClient contentFeignClient;

    public Product createProduct(Product product) {
        if (productRepository.existsBySku(product.getSku())) {
            throw new IllegalArgumentException("Product with SKU '" + product.getSku() + "' already exists");
        }
        return productRepository.save(product);
    }

    public Product getProduct(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product updateProduct(UUID productId, Product updatedProduct) {
        Product existing = getProduct(productId);
        existing.setSku(updatedProduct.getSku());
        existing.setName(updatedProduct.getName());
        existing.setPrice(updatedProduct.getPrice());
        existing.setCurrency(updatedProduct.getCurrency());
        existing.setStatus(updatedProduct.getStatus());
        return productRepository.save(existing);
    }

    public void deleteProduct(UUID productId) {
        if (!productRepository.existsById(productId)) {
            throw new EntityNotFoundException("Product not found with ID: " + productId);
        }
        productRepository.deleteById(productId);
    }

    @Transactional
    public List<ProductImage> addImages(UUID productId, List<MultipartFile> files) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<ProductImage> savedImages = new ArrayList<>();
        int sortOrder = imageRepository.findByProductProductId(productId).size();

        for (MultipartFile file : files) {
            Map<String, Object> contentResponse = contentFeignClient.uploadFile(file, true);

            String imageUrl = (String) contentResponse.get("filePath"); // adjust if your API returns "url"

            ProductImage image = ProductImage.builder()
                    .product(product)
                    .imageUrl(imageUrl)
                    .sortOrder(sortOrder++)
                    .build();

            savedImages.add(imageRepository.save(image));
        }

        return savedImages;
    }
}