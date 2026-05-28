package com.sanjukta.ordersystem.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemRequest {

    @NotNull(message = "product Id id required!")
    private Long productId;

    @NotNull(message = "quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}
