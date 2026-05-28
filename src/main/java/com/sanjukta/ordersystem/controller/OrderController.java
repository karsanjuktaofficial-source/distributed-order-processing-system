package com.sanjukta.ordersystem.controller;

import com.sanjukta.ordersystem.dto.CreateOrderRequest;
import com.sanjukta.ordersystem.entity.Order;
import com.sanjukta.ordersystem.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private OrderService orderService;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> getOrders() {
        orderService.getAllOrders();
        return orderService.getAllOrders();
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequest request, Authentication authentication) {
  //      System.out.println(authentication.toString());
        Order order = orderService.createOrder(request, authentication.getName());
        return ResponseEntity.ok().body(order);

    }


}
