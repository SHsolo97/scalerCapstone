package com.ecommerce.orderservice.services;

import com.ecommerce.orderservice.dto.request.OrderCreateRequest;
import com.ecommerce.orderservice.models.Order;
import com.ecommerce.orderservice.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Order createOrder(OrderCreateRequest request, User user);
    Page<Order> getOrdersForUser(User user, Pageable pageable);
    Order getOrderDetails(Long orderId, User user);
    // Methods for handling Kafka events - names based on spec
    void handlePaymentConfirmed(Object payload); // Using Object for now, will be specific payload type
    void handlePaymentFailed(Object payload); // Using Object for now, will be specific payload type
    void updateOrderShipment(Long orderId, String carrier, String trackingNumber);

}
