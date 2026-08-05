package com.food_delivery.order_service.client.dto;

import java.util.UUID;

public class RestaurantSummaryDto {

    private UUID id;
    private String name;

    public RestaurantSummaryDto() {
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
}
