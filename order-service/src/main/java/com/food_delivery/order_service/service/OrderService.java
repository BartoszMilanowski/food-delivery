package com.food_delivery.order_service.service;

import com.food_delivery.order_service.client.MenuItemClient;
import com.food_delivery.order_service.client.RestaurantClient;
import com.food_delivery.order_service.client.dto.MenuItemSummaryDto;
import com.food_delivery.order_service.dto.OrderItemRequestDto;
import com.food_delivery.order_service.dto.OrderItemResponseDto;
import com.food_delivery.order_service.dto.OrderRequestDto;
import com.food_delivery.order_service.dto.OrderResponseDto;
import com.food_delivery.order_service.exception.MenuItemNotAvailableException;
import com.food_delivery.order_service.exception.MenuItemNotFoundException;
import com.food_delivery.order_service.exception.OrderNotFoundException;
import com.food_delivery.order_service.exception.RestaurantNotFoundException;
import com.food_delivery.order_service.mapper.OrderItemMapper;
import com.food_delivery.order_service.mapper.OrderMapper;
import com.food_delivery.order_service.model.Order;
import com.food_delivery.order_service.model.OrderItem;
import com.food_delivery.order_service.model.OrderStatus;
import com.food_delivery.order_service.repository.OrderItemRepository;
import com.food_delivery.order_service.repository.OrderRepository;
import com.food_delivery.order_service.specification.OrderSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final RestaurantClient restaurantClient;
    private final MenuItemClient menuItemClient;

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            OrderMapper orderMapper,
            OrderItemMapper orderItemMapper,
            RestaurantClient restaurantClient,
            MenuItemClient menuItemClient
    ) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.restaurantClient = restaurantClient;
        this.menuItemClient = menuItemClient;
    }

    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto requestDto) {
        validateRestaurantExists(requestDto.getRestaurantId());

        List<ValidatedItem> validatedItems = requestDto.getItems()
                .stream()
                .map(item -> new ValidatedItem(item, fetchAvailableMenuItem(item.getMenuItemId())))
                .toList();

        BigDecimal totalPrice = validatedItems
                .stream()
                .map(vi -> vi.menuItemSummaryDto().getPrice().multiply(BigDecimal.valueOf(vi.request().getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = orderMapper.toEntity(requestDto, totalPrice);
        Order savedOrder = orderRepository.save(order);

        List<OrderItemResponseDto> itemsDtos = validatedItems
                .stream()
                .map(vi -> {
                    OrderItem orderItem = orderItemMapper.toEntity(vi.request(), vi.menuItemSummaryDto(), savedOrder);
                    OrderItem saved = orderItemRepository.save(orderItem);
                    return orderItemMapper.toDto(saved);
                })
                .toList();

        return orderMapper.toDto(savedOrder, itemsDtos);
    }

    @Transactional(readOnly = true)
    public OrderResponseDto getOrderById(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        List<OrderItemResponseDto> itmesDtos = mapOrderItems(id);

        return orderMapper.toDto(order, itmesDtos);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDto> searchOrders(
            UUID customerId,
            UUID restaurantId,
            OrderStatus status,
            Instant from,
            Instant to,
            String city
    ) {
        Specification<Order> spec = Specification
                .where(OrderSpecification.customerIdIsEqual(customerId))
                .and(OrderSpecification.restaurantIdIsEqual(restaurantId))
                .and(OrderSpecification.statusIsEqual(status))
                .and(OrderSpecification.createdBetween(from, to))
                .and(OrderSpecification.cityIsEqual(city));

        return orderRepository.findAll(spec)
                .stream()
                .map(order -> orderMapper.toDto(order, mapOrderItems(order.getId())))
                .toList();
    }

    @Transactional
    public OrderResponseDto updateOrderStatus(UUID id, OrderStatus newStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        order.setStatus(newStatus);
        Order updated = orderRepository.save(order);

        return orderMapper.toDto(updated, mapOrderItems(id));
    }

    private void validateRestaurantExists(UUID restaurantId) {
        try {
            restaurantClient.getRestaurantById(restaurantId);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new RestaurantNotFoundException(restaurantId);
        }
    }

    private MenuItemSummaryDto fetchAvailableMenuItem(UUID menuItemId) {
        MenuItemSummaryDto menuItem;

        try {
            menuItem = menuItemClient.getMenuItemById(menuItemId);
        } catch (HttpClientErrorException ex) {
            throw new MenuItemNotFoundException(menuItemId);
        }

        if (!menuItem.isAvailable()) {
            throw new MenuItemNotAvailableException(menuItemId);
        }

        return menuItem;
    }

    private List<OrderItemResponseDto> mapOrderItems(UUID orderId) {
        return orderItemRepository.findByOrderId(orderId)
                .stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    private record ValidatedItem(OrderItemRequestDto request, MenuItemSummaryDto menuItemSummaryDto) {
    }
}

