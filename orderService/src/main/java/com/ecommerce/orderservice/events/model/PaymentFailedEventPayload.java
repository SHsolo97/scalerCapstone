package com.ecommerce.orderservice.events.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentFailedEventPayload {
    private Long orderId;
    private String reason;
    private String gatewayErrorCode;
    private LocalDateTime timestamp;
}
