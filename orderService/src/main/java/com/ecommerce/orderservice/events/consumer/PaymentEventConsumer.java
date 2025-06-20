package com.ecommerce.orderservice.events.consumer;

import com.ecommerce.orderservice.events.model.PaymentConfirmedEventPayload;
import com.ecommerce.orderservice.events.model.PaymentFailedEventPayload;
import com.ecommerce.orderservice.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentEventConsumer {

    private final OrderService orderService;

    @KafkaListener(
        topics = "${app.kafka.topics.payment-confirmed:payment.confirmed}", // Default topic name
        groupId = "${spring.kafka.consumer.group-id:order-management-group}", // Default group ID
        // Ensure your KafkaConfig provides a ConcurrentKafkaListenerContainerFactory
        // that is configured for JSON deserialization of PaymentConfirmedEventPayload.
        // The properties approach for default.type is one way, or configure it in the factory.
        // Example: properties = {"spring.json.value.default.type=com.ecommerce.ordermanagement.event.model.PaymentConfirmedEventPayload"}
        containerFactory = "kafkaListenerContainerFactory" // Assuming you have a bean named this
    )
    public void listenPaymentConfirmed(@Payload PaymentConfirmedEventPayload payload,
                                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                       @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) { // Corrected to RECEIVED_PARTITION
        log.info("Received PaymentConfirmedEvent on topic {} partition {}: {}", topic, partition, payload);
        try {
            orderService.handlePaymentConfirmed(payload);
        } catch (Exception e) {
            log.error("Error processing PaymentConfirmedEvent for orderId {}: {}", payload.getOrderId(), e.getMessage(), e);
            // Consider dead-letter queue or other error handling strategies
        }
    }

    @KafkaListener(
        topics = "${app.kafka.topics.payment-failed:payment.failed}", // Default topic name
        groupId = "${spring.kafka.consumer.group-id:order-management-group}",
        // properties = {"spring.json.value.default.type=com.ecommerce.ordermanagement.event.model.PaymentFailedEventPayload"}
        containerFactory = "kafkaListenerContainerFactory" // Assuming you have a bean named this
    )
    public void listenPaymentFailed(@Payload PaymentFailedEventPayload payload,
                                    @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                    @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) { // Corrected to RECEIVED_PARTITION
        log.info("Received PaymentFailedEvent on topic {} partition {}: {}", topic, partition, payload);
        try {
            orderService.handlePaymentFailed(payload);
        } catch (Exception e) {
            log.error("Error processing PaymentFailedEvent for orderId {}: {}", payload.getOrderId(), e.getMessage(), e);
            // Consider dead-letter queue or other error handling strategies
        }
    }
}
