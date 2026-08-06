package com.food_delivery.order_service.mapper;

import com.food_delivery.order_service.dto.OrderRequestDto;
import com.food_delivery.order_service.dto.OrderResponseDto;
import com.food_delivery.order_service.dto.OrderItemResponseDto;
import com.food_delivery.order_service.model.Order;
import com.food_delivery.order_service.model.OrderStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class OrderMapper {

    public OrderResponseDto toDto(Order order, List<OrderItemResponseDto> orderItemResponseDtos) {
        OrderResponseDto dto = new OrderResponseDto();

        dto.setId(order.getId().toString());
        dto.setCustomerId(order.getCustomerId().toString());
        dto.setRestaurantId(order.getRestaurantId().toString());
        dto.setOrderStatus(order.getStatus().name());
        dto.setDeliveryStreet(order.getDeliveryStreet());
        dto.setDeliveryCity(order.getDeliveryCity());
        dto.setDeliveryPostalCode(order.getDeliveryPostalCode());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setOrderItems(orderItemResponseDtos);

        return dto;
    }

    public Order toEntity(OrderRequestDto requestDto, BigDecimal totalPrice) {
        Order order = new Order();

        order.setCustomerId(requestDto.getCustomerId());
        order.setRestaurantId(requestDto.getRestaurantId());
        order.setStatus(OrderStatus.CREATED);
        order.setDeliveryStreet(requestDto.getDeliveryStreet());
        order.setDeliveryCity(requestDto.getDeliveryCity());
        order.setDeliveryPostalCode(requestDto.getDeliveryPostalCode());
        order.setTotalPrice(totalPrice);

        return order;
    }
}