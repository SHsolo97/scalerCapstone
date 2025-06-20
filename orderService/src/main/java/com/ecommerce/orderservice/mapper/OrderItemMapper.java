package com.ecommerce.orderservice.mapper;

import com.ecommerce.orderservice.dto.OrderItemDto;
import com.ecommerce.orderservice.models.OrderItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    OrderItemDto toOrderItemDto(OrderItem orderItem);
    List<OrderItemDto> toOrderItemDtoList(List<OrderItem> orderItems);
}
