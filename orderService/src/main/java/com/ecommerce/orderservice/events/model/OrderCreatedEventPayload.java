package com.ecommerce.orderservice.events.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderCreatedEventPayload {
    private Long orderId;
    private Long userId;
    private BigDecimal totalAmount;
    private String currency;
    private List<OrderItemInfo> items;
    private PaymentDetailsInfo paymentDetails;
    private LocalDateTime timestamp;

    public OrderCreatedEventPayload(Long orderId, Long userId, BigDecimal totalAmount, String currency, List<OrderItemInfo> items, PaymentDetailsInfo paymentDetails, LocalDateTime timestamp) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.items = items;
        this.paymentDetails = paymentDetails;
        this.timestamp = timestamp;
    }

    @Data
    @NoArgsConstructor
    public static class OrderItemInfo {
        private Long productId;
        private String productName;
        private Integer quantity;
        private BigDecimal unitPrice;

        public OrderItemInfo(Long productId, String productName, Integer quantity, BigDecimal unitPrice) {
            this.productId = productId;
            this.productName = productName;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
        }
    }

    @Data
    @NoArgsConstructor
    public static class PaymentDetailsInfo {
        private String paymentMethodType;
        private String cardLast4;

        public PaymentDetailsInfo(String paymentMethodType, String cardLast4) {
            this.paymentMethodType = paymentMethodType;
            this.cardLast4 = cardLast4;
        }
    }
}
