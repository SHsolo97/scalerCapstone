package com.ecommerce.orderservice.events.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentConfirmedEventPayload {
    private Long orderId;
    private String transactionId;
    private String paymentMethod; // e.g., "CARD", "PAYPAL"
    private BigDecimal amount;
    private LocalDateTime timestamp;
}
