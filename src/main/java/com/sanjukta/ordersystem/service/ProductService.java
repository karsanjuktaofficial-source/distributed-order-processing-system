package com.sanjukta.ordersystem.service;

import com.sanjukta.ordersystem.dto.CreateProductRequest;
import com.sanjukta.ordersystem.dto.ProductResponse;
import com.sanjukta.ordersystem.entity.Product;
import com.sanjukta.ordersystem.exception.ResourceNotFoundException;
import com.sanjukta.ordersystem.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse findById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("Product not found!"));
        return mapToResponse(product);

    }
    public Product findByName(String name) {
        try {
            return productRepository.findByProductName(name);
        }
        catch (Exception e) {
            throw new ResourceNotFoundException("Product not found!");
        }
    }

    public List<ProductResponse> findAll() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::mapToResponse).toList();
    }
    public ProductResponse save(CreateProductRequest request) {
        Product p = productRepository.findByProductName(request.name());

        if(p!=null) {
                throw new RuntimeException("Product already exists, please update the existing product.");
        };

        return mapToResponse(productRepository.save(mapToEntity(request)));
    }

    public ProductResponse mapToResponse(Product product) {
        return new ProductResponse(product.getId(),
                product.getProductName(),
                product.getProductDescription(),
                product.getPrice(),
                product.getAvailableQuantity(),
                product.getCreatedAt()
        );
    }

    public Product mapToEntity(CreateProductRequest request) {
        Product product = new Product();
        product.setProductName(request.name());
        product.setProductDescription(request.description());
        product.setPrice(request.price());
        product.setAvailableQuantity(request.availableQuantity());
        product.setCreatedAt(LocalDateTime.now());
        product.setReservedQuantity(0);

        return product;
    }
}
