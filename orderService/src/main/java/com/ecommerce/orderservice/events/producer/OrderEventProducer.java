package com.ecommerce.orderservice.events.producer;

import com.ecommerce.orderservice.events.model.OrderCreatedEventPayload;
import com.ecommerce.orderservice.events.model.OrderStatusUpdatedEventPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.topics.order-events:order.events}") // Default topic name
    private String orderEventsTopic;

    public void sendOrderCreatedEvent(OrderCreatedEventPayload payload) {
        log.info("Sending OrderCreatedEvent for orderId: {} to topic: {}", payload.getOrderId(), orderEventsTopic);
        try {
            kafkaTemplate.send(orderEventsTopic, payload.getOrderId().toString(), payload);
        } catch (Exception e) {
            log.error("Error sending OrderCreatedEvent for orderId: {}: {}", payload.getOrderId(), e.getMessage(), e);
        }
    }

    public void sendOrderStatusUpdate(OrderStatusUpdatedEventPayload payload) {
        log.info("Sending OrderStatusUpdatedEvent for orderId: {} to topic: {}", payload.getOrderId(), orderEventsTopic);
        try {
            kafkaTemplate.send(orderEventsTopic, payload.getOrderId().toString(), payload);
        } catch (Exception e) {
            log.error("Error sending OrderStatusUpdatedEvent for orderId: {}: {}", payload.getOrderId(), e.getMessage(), e);
        }
    }
}
