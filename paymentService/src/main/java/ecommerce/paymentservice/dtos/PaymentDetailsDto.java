package ecommerce.paymentservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDetailsDto {
    private String paymentMethod;
    private String status;
}
