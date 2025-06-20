package com.ecommerce.orderservice.events.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
public class OrderStatusUpdatedEventPayload {
    private Long orderId;
    private Long userId;
    private String newStatus;
    private String oldStatus;
    private LocalDateTime timestamp;
    private Map<String, Object> details;

    public OrderStatusUpdatedEventPayload(Long orderId, Long userId, String newStatus, String oldStatus, LocalDateTime timestamp, Map<String, Object> details) {
        this.orderId = orderId;
        this.userId = userId;
        this.newStatus = newStatus;
        this.oldStatus = oldStatus;
        this.timestamp = timestamp;
        this.details = details;
    }
}
