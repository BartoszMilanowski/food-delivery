package com.food_delivery.order_service.client.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class MenuItemSummaryDto {

    private UUID id;
    private String name;
    private BigDecimal price;
    private boolean available;

    public MenuItemSummaryDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
