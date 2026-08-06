package com.food_delivery.order_service.exception;

import java.util.UUID;

public class MenuItemNotAvailableException extends RuntimeException {
    public MenuItemNotAvailableException(UUID id) {
        super("Menu item is not available: " + id);
    }
}
