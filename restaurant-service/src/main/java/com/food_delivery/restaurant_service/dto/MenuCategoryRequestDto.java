package com.food_delivery.restaurant_service.dto;

import com.food_delivery.restaurant_service.model.Restaurant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class MenuCategoryRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    private Integer displayOrder;

    @NotNull(message = "Restaurant is required")
    private UUID restaurantId;

    public MenuCategoryRequestDto(){}

    public MenuCategoryRequestDto(String name, Integer displayOrder, UUID restaurantId) {
        this.name = name;
        this.displayOrder = displayOrder;
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(UUID restaurantId) {
        this.restaurantId = restaurantId;
    }
}
