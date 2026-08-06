package com.food_delivery.order_service.mapper;

import com.food_delivery.order_service.client.dto.MenuItemSummaryDto;
import com.food_delivery.order_service.dto.OrderItemRequestDto;
import com.food_delivery.order_service.dto.OrderItemResponseDto;
import com.food_delivery.order_service.model.Order;
import com.food_delivery.order_service.model.OrderItem;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderItemMapperTest {

    private final OrderItemMapper orderItemMapper = new OrderItemMapper();

    @Test
    void toDto_shouldMapAllFields() {
        UUID orderItemId = UUID.randomUUID();
        UUID menuItemId = UUID.randomUUID();

        OrderItem orderItem = new OrderItem();

        ReflectionTestUtils.setField(orderItem, "id", orderItemId);
        orderItem.setMenuItemId(menuItemId);
        orderItem.setMenuItemName("Prosciutto Crudo");
        orderItem.setUnitPrice(BigDecimal.valueOf(35.50));
        orderItem.setQuantity(2);

        OrderItemResponseDto result = orderItemMapper.toDto(orderItem);

        assertThat(result.getId()).isEqualTo(orderItemId.toString());
        assertThat(result.getMenuItemId()).isEqualTo(menuItemId.toString());
        assertThat(result.getMenuItemName()).isEqualTo("Prosciutto Crudo");
        assertThat(result.getUnitPrice()).isEqualTo(BigDecimal.valueOf(35.50));
        assertThat(result.getQuantity()).isEqualTo(2);
    }

    @Test
    void toEntity_shouldMapAllFields() {
        UUID orderId = UUID.randomUUID();
        UUID menuItemId = UUID.randomUUID();

        Order order = new Order();
        ReflectionTestUtils.setField(order, "id", orderId);

        MenuItemSummaryDto menuItemSummaryDto = new MenuItemSummaryDto();
        menuItemSummaryDto.setId(menuItemId);
        menuItemSummaryDto.setName("Prosciutto Crudo");
        menuItemSummaryDto.setPrice(BigDecimal.valueOf(35.50));
        menuItemSummaryDto.setAvailable(true);

        OrderItemRequestDto requestDto = new OrderItemRequestDto();
        requestDto.setMenuItemId(menuItemId);
        requestDto.setQuantity(2);

        OrderItem result = orderItemMapper.toEntity(requestDto, menuItemSummaryDto, order);

        assertThat(result.getOrder().getId()).isEqualTo(orderId);
        assertThat(result.getMenuItemId()).isEqualTo(menuItemId);
        assertThat(result.getMenuItemName()).isEqualTo("Prosciutto Crudo");
        assertThat(result.getUnitPrice()).isEqualTo(BigDecimal.valueOf(35.50));
        assertThat(result.getQuantity()).isEqualTo(2);
    }
}
