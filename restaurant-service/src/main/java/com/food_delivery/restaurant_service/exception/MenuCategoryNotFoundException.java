package com.food_delivery.restaurant_service.exception;

import java.util.UUID;

public class MenuCategoryNotFoundException extends RuntimeException {
    public MenuCategoryNotFoundException(UUID id) {
        super("Menu Category not found: " + id);
    }
}
