package ecommerce.paymentservice.services;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import ecommerce.paymentservice.dtos.OrderDto;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Primary
public class RazorpayPaymentService implements PaymentService {
    private final RazorpayClient razorpayClient;
    private final RestTemplate restTemplate;
    private final String orderServiceUrl;

    public RazorpayPaymentService(RazorpayClient razorpayClient, RestTemplate restTemplate,
                                  @Value("${orderservice.url}") String orderServiceUrl) {
        this.razorpayClient = razorpayClient;
        this.restTemplate = restTemplate;
        this.orderServiceUrl = orderServiceUrl;
    }

    @Override
    public String createPaymentLink(Long orderId) throws RazorpayException {

        try {
            OrderDto order = restTemplate.getForObject(orderServiceUrl + "/orders/" + orderId, OrderDto.class);

            if (order == null) {
                throw new RuntimeException("Order not found");
            }

            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", order.getTotalAmount().multiply(new java.math.BigDecimal(100)));
            paymentLinkRequest.put("currency", order.getCurrency());
            paymentLinkRequest.put("accept_partial", false);
            paymentLinkRequest.put("expire_by", System.currentTimeMillis() + 15 * 60 * 1000);
            paymentLinkRequest.put("reference_id", orderId);
            paymentLinkRequest.put("description", "Payment for order no " + orderId);

            JSONObject customer = new JSONObject();
            customer.put("name", order.getUser().getName());
            customer.put("contact", "+91" + order.getUser().getPhone());
            customer.put("email", order.getUser().getEmail());
            paymentLinkRequest.put("customer", customer);

            JSONObject notify = new JSONObject();
            notify.put("sms", true);
            notify.put("email", true);
            paymentLinkRequest.put("reminder_enable", true);

            JSONObject notes = new JSONObject();
            notes.put("order_id", orderId);
            paymentLinkRequest.put("notes", notes);

            paymentLinkRequest.put("callback_url", "https://naman.dev/");
            paymentLinkRequest.put("callback_method", "get");

            PaymentLink payment = razorpayClient.paymentLink.create(paymentLinkRequest);
            return payment.get("short_url");
        } catch (Exception e) {
            // Ideally, log the exception
            throw new RuntimeException("Error creating payment link", e);
        }
    }

    @Override
    public String getPaymentStatus(String paymentId) {
        // ... (implementation for getting payment status)
        return null;
    }
}
