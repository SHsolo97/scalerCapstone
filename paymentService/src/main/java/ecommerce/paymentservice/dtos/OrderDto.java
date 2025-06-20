package ecommerce.paymentservice.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderDto {
    private Long id;
    private UserDto user;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private String currency;
    private AddressDto shippingAddress;
    private AddressDto billingAddress;
    private PaymentDetailsDto paymentMethodDetails;
    private String paymentTransactionId;
    private String shippingCarrier;
    private String shippingTrackingNumber;
    private List<OrderItemDto> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
