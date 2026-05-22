package com.sanjukta.ordersystem.dto;

import com.sanjukta.ordersystem.entity.OrderItem;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {
    private List<OrderItemRequest> orderItemRequests;
}
