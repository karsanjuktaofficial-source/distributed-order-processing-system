package com.sanjukta.ordersystem.service;

import com.sanjukta.ordersystem.dto.CreateOrderRequest;
import com.sanjukta.ordersystem.dto.OrderItemRequest;
import com.sanjukta.ordersystem.dto.OrderResponse;
import com.sanjukta.ordersystem.entity.*;
import com.sanjukta.ordersystem.event.OrderCreatedEvent;
import com.sanjukta.ordersystem.exception.InsufficientInventoryException;
import com.sanjukta.ordersystem.exception.ResourceNotFoundException;
import com.sanjukta.ordersystem.producer.OrderEventProducer;
import com.sanjukta.ordersystem.repository.OrderRepository;
import com.sanjukta.ordersystem.repository.ProductRepository;
import com.sanjukta.ordersystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OrderService {

    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;

    private final OrderEventProducer orderEventProducer;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, UserRepository userRepository,OrderEventProducer orderEventProducer) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderEventProducer = orderEventProducer;
    }

    public List<Order> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders;
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<OrderItemRequest> orderItemRequests = request.getOrderItemRequests();

        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setUser(user);

        //take each orderItemRequest and map it to a product
        //then calculate totalPrice of each item
        List<OrderItem> orderItemList = new ArrayList<>();

        for (OrderItemRequest orderItemRequest : orderItemRequests) {

            Product product = productRepository.findById(orderItemRequest.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            if(product.getAvailableQuantity()<orderItemRequest.getQuantity()){
                throw new InsufficientInventoryException("Sorry! Insufficient Stock for: "+product.getProductName());
            }


            //available minus requestQuantity
            //reserved plus requestQuantity
            product.setAvailableQuantity(product.getAvailableQuantity()-orderItemRequest.getQuantity());
            product.setReservedQuantity(product.getReservedQuantity()+orderItemRequest.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(orderItemRequest.getQuantity());
            orderItem.setProduct(product);

            orderItemList.add(orderItem);
        }

        order.setStatus(OrderStatus.PENDING);
        order.setOrderItems(orderItemList);
        order.setTotalPrice(calculateTotalPrice(orderItemList));

        Order savedOrder = orderRepository.save(order);

        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(
                savedOrder.getId(),
                savedOrder.getUser().getId(),
                savedOrder.getTotalPrice(),
                savedOrder.getOrderDate()//,
                //savedOrder.getStatus()
        );

        log.info(
                "Publishing ORDER_CREATED event for order {}",
                savedOrder.getId()
        );

        orderEventProducer.publishOrderCreatedEvent(orderCreatedEvent);

        OrderResponse orderResponse = new OrderResponse(
                savedOrder.getId(),
                savedOrder.getTotalPrice(),
                savedOrder.getOrderDate(),
                savedOrder.getStatus()
        );
        return orderResponse;
    }

    private BigDecimal calculateTotalPrice(List<OrderItem> orderItemList) {

        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderItem orderItem : orderItemList) {
            totalPrice = totalPrice.add(orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
        }
        return totalPrice;

    }
}

