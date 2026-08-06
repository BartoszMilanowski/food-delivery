package com.food_delivery.order_service.mapper;

import com.food_delivery.order_service.client.dto.MenuItemSummaryDto;
import com.food_delivery.order_service.dto.OrderItemRequestDto;
import com.food_delivery.order_service.dto.OrderItemResponseDto;
import com.food_delivery.order_service.model.Order;
import com.food_delivery.order_service.model.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {

    public OrderItemResponseDto toDto(OrderItem orderItem) {
        OrderItemResponseDto dto = new OrderItemResponseDto();

        dto.setId(orderItem.getId().toString());
        dto.setMenuItemId(orderItem.getMenuItemId().toString());
        dto.setMenuItemName(orderItem.getMenuItemName());
        dto.setUnitPrice(orderItem.getUnitPrice());
        dto.setQuantity(orderItem.getQuantity());

        return dto;
    }

    public OrderItem toEntity(OrderItemRequestDto dto, MenuItemSummaryDto menuItemSummaryDto, Order order) {
        OrderItem orderItem = new OrderItem();

        orderItem.setOrder(order);
        orderItem.setMenuItemId(menuItemSummaryDto.getId());
        orderItem.setMenuItemName(menuItemSummaryDto.getName());
        orderItem.setUnitPrice(menuItemSummaryDto.getPrice());
        orderItem.setQuantity(dto.getQuantity());

        return orderItem;
    }
}
