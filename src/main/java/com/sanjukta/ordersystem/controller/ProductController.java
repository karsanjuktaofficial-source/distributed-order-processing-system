package com.sanjukta.ordersystem.controller;

import com.sanjukta.ordersystem.dto.CreateProductRequest;
import com.sanjukta.ordersystem.dto.ProductResponse;
import com.sanjukta.ordersystem.entity.Product;
import com.sanjukta.ordersystem.repository.ProductRepository;
import com.sanjukta.ordersystem.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/products")
public class ProductController {

    private ProductService productService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductResponse> findAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ProductResponse findById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PostMapping
    public ProductResponse save(@Valid @RequestBody CreateProductRequest request) {
        return productService.save(request);
    }

    @PostMapping("/addToInventory")
    public ProductResponse addToInventory(@RequestParam String productName,@RequestParam Integer quantity) {
        return productService.addToInventory(productName,quantity);
    }

}
