package com.ecommerce.orderservice.services.impl;

import com.ecommerce.orderservice.dto.request.OrderCreateRequest;
import com.ecommerce.orderservice.models.Order;
import com.ecommerce.orderservice.models.OrderItem;
import com.ecommerce.orderservice.models.OrderStatus;
import com.ecommerce.orderservice.events.model.OrderCreatedEventPayload;
import com.ecommerce.orderservice.events.model.OrderStatusUpdatedEventPayload;
import com.ecommerce.orderservice.events.model.PaymentConfirmedEventPayload;
import com.ecommerce.orderservice.events.model.PaymentFailedEventPayload;
import com.ecommerce.orderservice.events.producer.OrderEventProducer;
import com.ecommerce.orderservice.exceptions.OrderNotFoundException;
import com.ecommerce.orderservice.exceptions.ProductNotInStockException;
import com.ecommerce.orderservice.repositories.OrderRepository;
import com.ecommerce.orderservice.services.OrderService;
import com.ecommerce.orderservice.services.external.CartDetailsDto;
import com.ecommerce.orderservice.services.external.CartServiceClient;
import com.ecommerce.orderservice.services.external.ProductCatalogServiceClient;
import com.ecommerce.orderservice.services.external.ProductDetailsDto;
import com.ecommerce.orderservice.models.Address;
import com.ecommerce.orderservice.models.User;
import com.ecommerce.orderservice.repositories.AddressRepository;
import com.ecommerce.orderservice.mapper.AddressMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductCatalogServiceClient productCatalogServiceClient;
    private final CartServiceClient cartServiceClient;
    private final OrderEventProducer orderEventProducer;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Override
    @Transactional
    public Order createOrder(OrderCreateRequest request, User user) {
        log.info("Creating order for user: {}", user.getId());

        // Map and persist shipping address
        Address shippingAddress = addressMapper.toEntity(request.getShippingAddress());
        shippingAddress.setUser(user);
        addressRepository.save(shippingAddress);

        // Map and persist billing address
        Address billingAddress = addressMapper.toEntity(request.getBillingAddress());
        billingAddress.setUser(user);
        addressRepository.save(billingAddress);

        // Fetch cart details from Cart Service
        CartDetailsDto cartDetails = cartServiceClient.getCartDetails(user.getId()).block();

        if (cartDetails == null || cartDetails.getItems() == null || cartDetails.getItems().isEmpty()) {
            throw new ProductNotInStockException("Cart is empty or could not be fetched.");
        }

        Order order = new Order();
        order.setUser(user);
        // Optionally, store address IDs or keep AddressDto for denormalized storage
        order.setShippingAddress(request.getShippingAddress());
        order.setBillingAddress(request.getBillingAddress());
        order.setPaymentMethodDetails(request.getPaymentMethodDetails());
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        order.setCurrency("USD");

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (var cartItem : cartDetails.getItems()) {
            ProductDetailsDto productDetails = productCatalogServiceClient.getProductDetails(cartItem.getProductId()).block();
            if (productDetails == null) {
                throw new ProductNotInStockException("Product details not found for ID: " + cartItem.getProductId());
            }
            if (productDetails.getStockQuantity() < cartItem.getQuantity()) {
                throw new ProductNotInStockException("Not enough stock for product: " + productDetails.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductId(productDetails.getProductId());
            orderItem.setProductName(productDetails.getName());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(productDetails.getPrice());
            orderItem.setTotalPriceForItem(productDetails.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            orderItems.add(orderItem);

            totalAmount = totalAmount.add(orderItem.getTotalPriceForItem());
        }

        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);
        log.info("Order {} created successfully for user {}", savedOrder.getId(), user.getId());

        List<OrderCreatedEventPayload.OrderItemInfo> itemInfos = savedOrder.getItems().stream()
            .map(oi -> new OrderCreatedEventPayload.OrderItemInfo(oi.getProductId(), oi.getProductName(), oi.getQuantity(), oi.getUnitPrice()))
            .collect(Collectors.toList());

        OrderCreatedEventPayload payload = new OrderCreatedEventPayload(
            savedOrder.getId(),
            savedOrder.getUser().getId(),
            savedOrder.getTotalAmount(),
            savedOrder.getCurrency(),
            itemInfos,
            new OrderCreatedEventPayload.PaymentDetailsInfo(request.getPaymentMethodDetails().getPaymentMethodType(), request.getPaymentMethodDetails().getCardLast4()),
            LocalDateTime.now()
        );
        orderEventProducer.sendOrderCreatedEvent(payload);
        log.info("Published OrderCreatedEvent for orderId: {}", savedOrder.getId());
        
        cartServiceClient.clearCart(user.getId()).subscribe(
            success -> log.info("Cart cleared for user {}", user.getId()),
            error -> log.error("Error clearing cart for user {}: {}", user.getId(), error.getMessage())
        );

        return savedOrder;
    }

    @Override
    public Page<Order> getOrdersForUser(User user, Pageable pageable) {
        return orderRepository.findAllByUser(user, pageable);
    }

    @Override
    public Order getOrderDetails(Long orderId, User user) {
        return orderRepository.findByIdAndUser(orderId, user)
            .orElseThrow(() -> new OrderNotFoundException("Order not found for user"));
    }

    @Override
    @Transactional
    public void handlePaymentConfirmed(Object eventPayload) {
        PaymentConfirmedEventPayload payload = (PaymentConfirmedEventPayload) eventPayload;
        log.info("Handling PaymentConfirmedEvent for orderId: {}", payload.getOrderId());
        Order order = orderRepository.findById(payload.getOrderId())
            .orElseThrow(() -> {
                log.error("Order not found for payment confirmation, orderId: {}", payload.getOrderId());
                return new OrderNotFoundException("Order not found: " + payload.getOrderId());
            });

        if (order.getStatus() == OrderStatus.PENDING_PAYMENT) {
            order.setStatus(OrderStatus.PAID);
            order.setPaymentTransactionId(payload.getTransactionId());
            orderRepository.save(order);
            log.info("Order {} status updated to PAID", order.getId());

            OrderStatusUpdatedEventPayload statusUpdatePayload = new OrderStatusUpdatedEventPayload(
                order.getId(),
                order.getUser().getId(),
                OrderStatus.PAID.name(),
                OrderStatus.PENDING_PAYMENT.name(),
                LocalDateTime.now(),
                Map.of("transactionId", payload.getTransactionId())
            );
            orderEventProducer.sendOrderStatusUpdate(statusUpdatePayload);
            log.info("Published OrderStatusUpdatedEvent for orderId: {} to PAID", order.getId());
        } else {
            log.warn("PaymentConfirmedEvent received for order {} which is not in PENDING_PAYMENT state. Current state: {}", order.getId(), order.getStatus());
        }
    }

    @Override
    @Transactional
    public void handlePaymentFailed(Object eventPayload) {
        PaymentFailedEventPayload payload = (PaymentFailedEventPayload) eventPayload;
        log.info("Handling PaymentFailedEvent for orderId: {}. Reason: {}", payload.getOrderId(), payload.getReason());
        Order order = orderRepository.findById(payload.getOrderId())
            .orElseThrow(() -> {
                log.error("Order not found for payment failure, orderId: {}", payload.getOrderId());
                return new OrderNotFoundException("Order not found: " + payload.getOrderId());
            });

        order.setStatus(OrderStatus.FAILED);
        orderRepository.save(order);
        log.info("Order {} status updated to FAILED", order.getId());

        OrderStatusUpdatedEventPayload statusUpdatePayload = new OrderStatusUpdatedEventPayload(
            order.getId(),
            order.getUser().getId(),
            OrderStatus.FAILED.name(),
            order.getStatus().name(), // Previous status
            LocalDateTime.now(),
            Map.of("reason", payload.getReason(), "gatewayErrorCode", String.valueOf(payload.getGatewayErrorCode()))
        );
        orderEventProducer.sendOrderStatusUpdate(statusUpdatePayload);
        log.info("Published OrderStatusUpdatedEvent for orderId: {} to FAILED", order.getId());
    }

    @Override
    @Transactional
    public void updateOrderShipment(Long orderId, String carrier, String trackingNumber) {
        log.info("Updating shipment for orderId: {}. Carrier: {}, Tracking: {}", orderId, carrier, trackingNumber);
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException("Order not found: " + orderId));

        // Add logic to check if order can be shipped (e.g., must be PAID or PROCESSING)
        if (order.getStatus() != OrderStatus.PAID && order.getStatus() != OrderStatus.PROCESSING) {
            log.warn("Order {} cannot be shipped as its current status is {}.", orderId, order.getStatus());
            throw new IllegalStateException("Order cannot be shipped in its current state: " + order.getStatus());
        }

        order.setStatus(OrderStatus.SHIPPED);
        order.setShippingCarrier(carrier);
        order.setShippingTrackingNumber(trackingNumber);
        orderRepository.save(order);
        log.info("Order {} status updated to SHIPPED", order.getId());

        // Publish OrderStatusUpdatedEvent
        OrderStatusUpdatedEventPayload statusUpdatePayload = new OrderStatusUpdatedEventPayload(
            order.getId(),
            order.getUser().getId(),
            order.getStatus().name(),
            null, // Previous status could be PAID or PROCESSING
            LocalDateTime.now(),
            Map.of("shippingCarrier", carrier, "shippingTrackingNumber", trackingNumber)
        );
        orderEventProducer.sendOrderStatusUpdate(statusUpdatePayload);
        log.info("Published OrderStatusUpdatedEvent for orderId: {} to SHIPPED", order.getId());
    }
}
