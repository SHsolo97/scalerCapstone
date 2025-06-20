package com.ecommerce.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Represents payment details provided during order creation")
public class PaymentDetailsDto {
    @Schema(description = "Type of payment method used", example = "CARD")
    private String paymentMethodType; // e.g., "CARD", "PAYPAL"

    @Schema(description = "Last 4 digits of the credit card, if applicable", example = "1234", nullable = true)
    private String cardLast4;

    @Schema(description = "Brand of the credit card, if applicable", example = "Visa", nullable = true)
    private String cardBrand;
    // any other non-sensitive details
}
