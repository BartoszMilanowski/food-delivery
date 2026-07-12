package com.food_delivery.restaurant_service.repositories;

import com.food_delivery.restaurant_service.model.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MenuCategoryRepository extends JpaRepository<MenuCategory, UUID> {
    List<MenuCategory> findByRestaurantIdOrderByDisplayOrderAsc(UUID restaurantId);
}
