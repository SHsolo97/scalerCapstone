package com.ecommerce.orderservice.controllers;

import com.ecommerce.orderservice.dto.request.OrderCreateRequest;
import com.ecommerce.orderservice.dto.response.OrderResponse;
import com.ecommerce.orderservice.dto.response.OrderSummaryResponse;
import com.ecommerce.orderservice.models.Order;
import com.ecommerce.orderservice.mapper.OrderMapper;
import com.ecommerce.orderservice.models.User;
import com.ecommerce.orderservice.repositories.UserRepository;
import com.ecommerce.orderservice.services.OrderService;
import com.ecommerce.orderservice.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@Tag(name = "Order Management", description = "APIs for creating, retrieving, and managing orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;
    private final UserRepository userRepository;


    public OrderController(OrderService orderService, OrderMapper orderMapper, UserRepository userRepository) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
        this.userRepository = userRepository;
    }

    @Operation(summary = "Create a new order", description = "Creates a new order based on the user's cart and provided details.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Order created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Cart not found or product not found/out of stock")
    })
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
        @Parameter(description = "Order creation request payload", required = true) @Valid @RequestBody OrderCreateRequest request,
        @AuthenticationPrincipal Jwt jwt
    ) {
        Long userId = JwtUtil.getUserIdFromJwt();
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Order createdOrder = orderService.createOrder(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderMapper.toOrderResponse(createdOrder));
    }

    @Operation(summary = "Get orders for a user", description = "Retrieves a paginated list of orders for the specified user.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of orders",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))) // Note: Schema for Page<OrderSummaryResponse> is more complex
    })
    @GetMapping
    public ResponseEntity<Page<OrderSummaryResponse>> getUserOrders(
        Pageable pageable,
        @AuthenticationPrincipal Jwt jwt
    ) {
        Long userId = JwtUtil.getUserIdFromJwt();
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Page<Order> ordersPage = orderService.getOrdersForUser(user, pageable);
        Page<OrderSummaryResponse> responsePage = ordersPage.map(orderMapper::toOrderSummaryResponse);
        return ResponseEntity.ok(responsePage);
    }

    @Operation(summary = "Get order details", description = "Retrieves detailed information for a specific order.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved order details",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponse.class))),
        @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderDetails(
        @Parameter(description = "ID of the order to retrieve", required = true) @PathVariable Long orderId,
        @AuthenticationPrincipal Jwt jwt
    ) {
        Long userId = JwtUtil.getUserIdFromJwt();
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Order order = orderService.getOrderDetails(orderId, user);
        return ResponseEntity.ok(orderMapper.toOrderResponse(order));
    }

    @Operation(summary = "Mark order as shipped (Admin)", description = "Updates the order status to SHIPPED and adds tracking information. Requires admin privileges.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order marked as shipped successfully"),
        @ApiResponse(responseCode = "404", description = "Order not found"),
        @ApiResponse(responseCode = "403", description = "Forbidden - User does not have admin privileges (conceptual)")
    })
    @PostMapping("/{orderId}/ship")
    public ResponseEntity<Void> markOrderAsShipped(
        @Parameter(description = "ID of the order to mark as shipped", required = true) @PathVariable Long orderId,
        @RequestParam String carrier,
        @RequestParam String trackingNumber
    ) {
        orderService.updateOrderShipment(orderId, carrier, trackingNumber);
        return ResponseEntity.ok().build();
    }
}
