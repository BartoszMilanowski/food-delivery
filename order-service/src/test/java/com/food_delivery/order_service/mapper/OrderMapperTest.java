package com.food_delivery.order_service.mapper;

import com.food_delivery.order_service.dto.OrderItemResponseDto;
import com.food_delivery.order_service.dto.OrderRequestDto;
import com.food_delivery.order_service.dto.OrderResponseDto;
import com.food_delivery.order_service.model.Order;
import com.food_delivery.order_service.model.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderMapperTest {

    private final OrderMapper orderMapper = new OrderMapper();

    @Test
    void toDto_shouldMapAllFields() {
        UUID orderId = UUID.randomUUID();
        UUID orderItemId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        Instant createdAt = Instant.now();


        Order order = new Order();
        ReflectionTestUtils.setField(order, "id", orderId);
        order.setCustomerId(customerId);
        order.setRestaurantId(restaurantId);
        order.setStatus(OrderStatus.CREATED);
        order.setDeliveryStreet("Główna 5");
        order.setDeliveryCity("Warszawa");
        order.setDeliveryPostalCode("00-001");
        order.setTotalPrice(BigDecimal.valueOf(71));
        order.setCreatedAt(createdAt);

        OrderItemResponseDto orderItemResponseDto = new OrderItemResponseDto();
        orderItemResponseDto.setId(orderItemId.toString());
        List<OrderItemResponseDto> orderItemResponseDtos = List.of(orderItemResponseDto);

        OrderResponseDto result = orderMapper.toDto(order, orderItemResponseDtos);

        assertThat(result.getId()).isEqualTo(orderId.toString());
        assertThat(result.getCustomerId()).isEqualTo(customerId.toString());
        assertThat(result.getRestaurantId()).isEqualTo(restaurantId.toString());
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.CREATED.name());
        assertThat(result.getDeliveryStreet()).isEqualTo("Główna 5");
        assertThat(result.getDeliveryCity()).isEqualTo("Warszawa");
        assertThat(result.getDeliveryPostalCode()).isEqualTo("00-001");
        assertThat(result.getTotalPrice()).isEqualTo(BigDecimal.valueOf(71));
        assertThat(result.getCreatedAt()).isEqualTo(createdAt);
        assertThat(result.getOrderItems()).isEqualTo(orderItemResponseDtos);
    }

    @Test
    void toEntity_shouldMapAllFields() {
        UUID customerId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();

        OrderRequestDto requestDto = new OrderRequestDto();
        requestDto.setCustomerId(customerId);
        requestDto.setRestaurantId(restaurantId);
        requestDto.setDeliveryStreet("Główna 5");
        requestDto.setDeliveryCity("Warszawa");
        requestDto.setDeliveryPostalCode("00-001");

        BigDecimal totalPrice = BigDecimal.valueOf(71);

        Order result = orderMapper.toEntity(requestDto, totalPrice);

        assertThat(result.getCustomerId()).isEqualTo(customerId);
        assertThat(result.getRestaurantId()).isEqualTo(restaurantId);
        assertThat(result.getStatus()).isEqualTo(OrderStatus.CREATED);
        assertThat(result.getDeliveryStreet()).isEqualTo("Główna 5");
        assertThat(result.getDeliveryCity()).isEqualTo("Warszawa");
        assertThat(result.getDeliveryPostalCode()).isEqualTo("00-001");
        assertThat(result.getTotalPrice()).isEqualTo(BigDecimal.valueOf(71));
    }


}
