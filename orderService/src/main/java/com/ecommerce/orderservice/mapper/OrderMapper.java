package com.ecommerce.orderservice.mapper;

import com.ecommerce.orderservice.dto.response.OrderResponse;
import com.ecommerce.orderservice.dto.response.OrderSummaryResponse;
import com.ecommerce.orderservice.models.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "status", target = "status") // Enum to String
    OrderResponse toOrderResponse(Order order);

    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "createdAt", target = "orderDate")
    @Mapping(source = "status", target = "status")
    @Mapping(target = "itemCount", expression = "java(order.getItems() != null ? order.getItems().size() : 0)")
    OrderSummaryResponse toOrderSummaryResponse(Order order);

    List<OrderSummaryResponse> toOrderSummaryResponseList(List<Order> orders);
}
