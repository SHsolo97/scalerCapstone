package com.ecommerce.orderservice.services.external;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data Transfer Object for cart details, received from Cart Service")
public class CartDetailsDto {
    @Schema(description = "Unique identifier of the cart")
    private UUID cartId;

    @Schema(description = "Unique identifier of the user who owns the cart")
    private UUID userId;

    @Schema(description = "List of items in the cart")
    private List<CartItemDto> items;

    @Schema(description = "Total amount for all items in the cart", example = "150.75")
    private BigDecimal totalAmount;
}
