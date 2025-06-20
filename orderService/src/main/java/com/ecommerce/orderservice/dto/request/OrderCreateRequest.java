package com.ecommerce.orderservice.dto.request;

import com.ecommerce.orderservice.dto.AddressDto;
import com.ecommerce.orderservice.dto.PaymentDetailsDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Request payload for creating a new order")
public class OrderCreateRequest {
    @NotNull @Valid
    @Schema(description = "Shipping address for the order", requiredMode = Schema.RequiredMode.REQUIRED)
    private AddressDto shippingAddress;

    @NotNull @Valid
    @Schema(description = "Billing address for the order", requiredMode = Schema.RequiredMode.REQUIRED)
    private AddressDto billingAddress;

    @NotNull @Valid
    @Schema(description = "Payment details for the order", requiredMode = Schema.RequiredMode.REQUIRED)
    private PaymentDetailsDto paymentMethodDetails;
}
