package ecommerce.paymentservice.services;

import com.razorpay.RazorpayException;

public interface PaymentService {


    String createPaymentLink(Long orderId) throws RazorpayException;

    String getPaymentStatus(String paymentId);


}
