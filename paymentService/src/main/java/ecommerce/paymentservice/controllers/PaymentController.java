package ecommerce.paymentservice.controllers;

import com.razorpay.RazorpayException;
import ecommerce.paymentservice.services.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    private PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/{orderid}")
    public String createPaymentLink(@PathVariable Long orderId) throws RazorpayException {
        String link = paymentService.createPaymentLink(orderId);
        return link;
    }

    @PostMapping("/webhook")
    public void handleWebhookEvent(@RequestBody Map<String, String> webhookEvent) {
        System.out.println(webhookEvent);
    }

}
