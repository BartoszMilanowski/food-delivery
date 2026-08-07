package com.food_delivery.order_service.controller;

import com.food_delivery.order_service.dto.OrderRequestDto;
import com.food_delivery.order_service.dto.OrderResponseDto;
import com.food_delivery.order_service.dto.OrderStatusUpdateRequestDto;
import com.food_delivery.order_service.model.OrderStatus;
import com.food_delivery.order_service.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Tag(name = "Order", description = "API for managing orders")
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Create a new order")
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(requestDto));
    }

    @Operation(summary = "Get order by Id")
    @GetMapping("/id")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @Operation(summary = "Search orders by customer, restaurant, status, date range and city")
    @GetMapping("/search")
    public ResponseEntity<List<OrderResponseDto>> searchOrders(
            @RequestParam(required = false) UUID customerId,
            @RequestParam(required = false) UUID restaurantId,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) Instant from,
            @RequestParam(required = false) Instant to,
            @RequestParam(required = false) String city
    ) {
        return ResponseEntity.ok(orderService.searchOrders(
                customerId,
                restaurantId,
                status,
                from,
                to,
                city
        ));
    }

    @Operation(summary = "Update order status")
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(
            @PathVariable UUID id,
            @Valid @RequestBody OrderStatusUpdateRequestDto requestDto
    ) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, requestDto.getStatus()));
    }
}
