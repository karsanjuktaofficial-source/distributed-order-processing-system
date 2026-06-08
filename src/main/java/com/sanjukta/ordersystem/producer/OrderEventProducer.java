package com.sanjukta.ordersystem.producer;

import com.sanjukta.ordersystem.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final String TOPIC = "order-created-topic";

    public void publishOrderCreatedEvent(OrderCreatedEvent orderCreatedEvent) {
        kafkaTemplate.send(TOPIC, orderCreatedEvent);

        log.info("Order Created Event sent to topic {}", TOPIC);
    }
}
