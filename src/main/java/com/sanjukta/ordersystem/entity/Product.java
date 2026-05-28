package com.sanjukta.ordersystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String productName;
    @Column
    private String productDescription;
    @Column(nullable = false)
    private BigDecimal price;

    private LocalDateTime createdAt;

    private Integer availableQuantity;

    private int reservedQuantity;

    @Version
    private Long version;



}
