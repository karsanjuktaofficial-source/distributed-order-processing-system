package com.sanjukta.ordersystem.dto;

import com.sanjukta.ordersystem.entity.OrderItem;
import com.sanjukta.ordersystem.entity.OrderStatus;
import com.sanjukta.ordersystem.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse (Long orderId,
        BigDecimal totalPrice,
        LocalDateTime orderDate,
        OrderStatus orderStatus){



}
