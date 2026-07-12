package com.food_delivery.restaurant_service.repositories;

import com.food_delivery.restaurant_service.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MenuItemRepository extends JpaRepository<MenuItem, UUID> {
    List<MenuItem> findByRestaurantId(UUID restaurantId);
    List<MenuItem> findByRestaurantIdAndAvailableTrue(UUID restaurantId);
    List<MenuItem> findByCategoryId(UUID categoryId);
}
