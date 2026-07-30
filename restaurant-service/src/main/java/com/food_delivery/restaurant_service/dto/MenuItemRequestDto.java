package com.food_delivery.restaurant_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public class MenuItemRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    @Size(max = 1000, message = "Description cannot be longer than 1000 characters")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be grater than 0.0")
    private BigDecimal price;

    private boolean available;

    private String imageUrl;

    @NotNull(message = "Menu category is required")
    private UUID categoryId;

    @NotNull(message = "Restaurant is required")
    private UUID restaurantId;

    public MenuItemRequestDto(){}

    public MenuItemRequestDto(String name, String description, BigDecimal price, boolean available,
                              String imageUrl, UUID categoryId, UUID restaurantId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.available = available;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(UUID restaurantId) {
        this.restaurantId = restaurantId;
    }
}
