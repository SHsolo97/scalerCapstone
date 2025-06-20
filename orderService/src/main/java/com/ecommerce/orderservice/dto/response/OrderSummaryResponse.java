package com.ecommerce.orderservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "Summary response object for an order, typically used in lists")
public class OrderSummaryResponse {
    @Schema(description = "Unique identifier of the order")
    private Long orderId;

    @Schema(description = "Current status of the order", example = "SHIPPED")
    private String status;

    @Schema(description = "Total amount of the order", example = "120.50")
    private BigDecimal totalAmount;

    @Schema(description = "Date and time when the order was placed")
    private LocalDateTime orderDate;

    @Schema(description = "Number of unique items in the order", example = "3")
    private int itemCount;
}
