package com.food_delivery.order_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    private UUID customerId;

    @NotNull
    private UUID restaurantId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @NotNull
    private String deliveryStreet;

    @NotNull
    private String deliveryCity;

    @NotNull
    private String deliveryPostalCode;

    @NotNull
    private BigDecimal totalPrice;

    @CreationTimestamp
    private Instant createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    public Order() {
    }

    public Order(UUID customerId, UUID restaurantId, OrderStatus status, String deliveryStreet,
                 String deliveryCity, String deliveryPostalCode, BigDecimal totalPrice,
                 Instant createdAt, List<OrderItem> orderItems) {
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.status = status;
        this.deliveryStreet = deliveryStreet;
        this.deliveryCity = deliveryCity;
        this.deliveryPostalCode = deliveryPostalCode;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.orderItems = orderItems;
    }

    public UUID getId() {
        return id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(UUID restaurantId) {
        this.restaurantId = restaurantId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getDeliveryStreet() {
        return deliveryStreet;
    }

    public void setDeliveryStreet(String deliveryStreet) {
        this.deliveryStreet = deliveryStreet;
    }

    public String getDeliveryCity() {
        return deliveryCity;
    }

    public void setDeliveryCity(String deliveryCity) {
        this.deliveryCity = deliveryCity;
    }

    public String getDeliveryPostalCode() {
        return deliveryPostalCode;
    }

    public void setDeliveryPostalCode(String deliveryPostalCode) {
        this.deliveryPostalCode = deliveryPostalCode;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
