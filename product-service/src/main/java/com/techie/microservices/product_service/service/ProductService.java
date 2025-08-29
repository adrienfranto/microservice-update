package com.techie.microservices.product_service.service;

import com.techie.microservices.product_service.dto.ProductRequest;
import com.techie.microservices.product_service.dto.ProductResponse;
import com.techie.microservices.product_service.model.Product;
import com.techie.microservices.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    // CREATE
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.name())
                .skuCode(productRequest.skuCode())
                .description(productRequest.description())
                .price(productRequest.price())
                .build();

        productRepository.save(product);
        log.info("Product created successfully: {}", product.getId());

        return mapToResponse(product);
    }

    // READ ALL
    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    // READ BY ID
    public ProductResponse getById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));

        return mapToResponse(product);
    }

    // UPDATE
    public ProductResponse updateProduct(String id, ProductRequest request) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));

        existing.setName(request.name());
        existing.setSkuCode(request.skuCode());
        existing.setDescription(request.description());
        existing.setPrice(request.price());

        productRepository.save(existing);
        log.info("Product updated: {}", id);

        return mapToResponse(existing);
    }

    // DELETE
    public void deleteProduct(String id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete: Product not found with ID: " + id);
        }
        productRepository.deleteById(id);

        log.info("Deleted product with ID: {}", id);
    }

    // Helper to map Product to ProductResponse
    private ProductResponse mapToResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getSkuCode(),
                product.getDescription(),
                product.getPrice()
        );
    }
}
