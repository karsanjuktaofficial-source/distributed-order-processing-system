package com.sanjukta.ordersystem.dto;

import com.sanjukta.ordersystem.entity.OrderItem;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {

    @Valid
    @NotNull(message = "At least one item is required!")
    private List<OrderItemRequest> orderItemRequests;
}
