package com.ecommerce.orderservice.services.external;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data Transfer Object for an item in the cart, received from Cart Service")
public class CartItemDto {
    @Schema(description = "Product ID of the cart item")
    private Long productId;

    @Schema(description = "Name of the product in the cart item", example = "Wireless Mouse")
    private String productName;

    @Schema(description = "Quantity of the product in the cart item", example = "2")
    private int quantity;

    @Schema(description = "Unit price of the product in the cart item", example = "25.99")
    private BigDecimal unitPrice;
}
