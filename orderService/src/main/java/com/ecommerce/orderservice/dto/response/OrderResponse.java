package com.ecommerce.orderservice.dto.response;

import com.ecommerce.orderservice.dto.AddressDto;
import com.ecommerce.orderservice.dto.OrderItemDto;
import com.ecommerce.orderservice.dto.PaymentDetailsDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "Detailed response object for an order")
public class OrderResponse {
    @Schema(description = "Unique identifier of the order")
    private Long orderId;

    @Schema(description = "Unique identifier of the user who placed the order")
    private Long userId;

    @Schema(description = "Current status of the order", example = "PENDING_PAYMENT")
    private String status;

    @Schema(description = "Total amount of the order", example = "199.99")
    private BigDecimal totalAmount;

    @Schema(description = "Currency of the total amount", example = "USD")
    private String currency;

    @Schema(description = "Shipping address for the order")
    private AddressDto shippingAddress;

    @Schema(description = "Billing address for the order")
    private AddressDto billingAddress;

    @Schema(description = "List of items included in the order")
    private List<OrderItemDto> items;

    @Schema(description = "Payment details used for the order")
    private PaymentDetailsDto paymentMethodDetails;

    @Schema(description = "Transaction ID from the payment gateway", nullable = true)
    private String paymentTransactionId;

    @Schema(description = "Shipping carrier name", nullable = true, example = "FedEx")
    private String shippingCarrier;

    @Schema(description = "Shipping tracking number", nullable = true, example = "782347234987")
    private String shippingTrackingNumber;

    @Schema(description = "Timestamp when the order was created")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the order was last updated")
    private LocalDateTime updatedAt;
}
