package com.food_delivery.restaurant_service.repositories;

import com.food_delivery.restaurant_service.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {
    List<Restaurant> findByCityAndCuisineType(String city, String cuisineType);
    List<Restaurant> findByOwnerId(UUID ownerId);
    List<Restaurant> findByActiveTrue();
}
