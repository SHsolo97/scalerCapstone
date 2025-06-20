package com.ecommerce.orderservice.services.external;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data Transfer Object for product details from the Product Catalog Service")
public class ProductDetailsDto {
    @Schema(description = "Unique identifier of the product")
    private Long productId;

    @Schema(description = "Name of the product", example = "Laptop Pro")
    private String name;

    @Schema(description = "Price of the product", example = "1200.99")
    private BigDecimal price;

    @Schema(description = "Available stock quantity of the product", example = "50")
    private int stockQuantity;
}
