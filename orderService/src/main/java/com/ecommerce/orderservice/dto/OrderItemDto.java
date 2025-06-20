package com.ecommerce.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Schema(description = "Represents an item within an order")
public class OrderItemDto {
    @Schema(description = "Unique identifier of the product")
    private Long productId;

    @Schema(description = "Name of the product", example = "Laptop Pro")
    private String productName;

    @Schema(description = "Quantity of this item ordered", example = "1")
    private Integer quantity;

    @Schema(description = "Price of a single unit of this item at the time of order", example = "999.99")
    private BigDecimal unitPrice;

    @Schema(description = "Total price for this line item (quantity * unitPrice)", example = "999.99")
    private BigDecimal totalPriceForItem;
}
