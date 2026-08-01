package com.food_delivery.order_service.specification;

import com.food_delivery.order_service.model.Order;
import com.food_delivery.order_service.model.OrderStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.UUID;

public class OrderSpecification {

    private OrderSpecification() {
    }

    public static Specification<Order> customerIdIsEqual(UUID customerId) {
        return (root, query, cb) ->
                customerId == null
                        ? cb.conjunction()
                        : cb.equal(root.get("customerId"), customerId);
    }

    public static Specification<Order> restaurantIdIsEqual(UUID restaurantId) {
        return (root, query, cb) ->
                restaurantId == null
                        ? cb.conjunction()
                        : cb.equal(root.get("restaurantId"), restaurantId);
    }

    public static Specification<Order> statusIsEqual(OrderStatus status) {
        return (root, query, cb) ->
                status == null
                        ? cb.conjunction()
                        : cb.equal(root.get("status"), status);
    }

    public static Specification<Order> createdBetween(Instant from, Instant to) {
        return (root, query, cb) -> {
            if (from != null && to != null) {
                return cb.between(root.get("createdAt"), from, to);
            }
            if (from != null) {
                return cb.greaterThanOrEqualTo(root.get("createdAt"), from);
            }
            if (to != null) {
                return cb.lessThanOrEqualTo(root.get("createdAt"), to);
            }
            return cb.conjunction();
        };
    }

    public static Specification<Order> cityIsEqual(String city) {
        return (root, query, cb) ->
                city == null
                        ? cb.conjunction()
                        : cb.equal(cb.lower(root.get("deliveryCity")), city.toLowerCase());
    }
}
