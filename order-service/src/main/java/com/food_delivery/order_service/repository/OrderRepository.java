package com.food_delivery.order_service.repository;

import com.food_delivery.order_service.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface OrderRepository extends
        JpaRepository<Order, UUID>,
        JpaSpecificationExecutor<Order> {
}
