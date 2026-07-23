package com.food_delivery.restaurant_service.repository;

import com.food_delivery.restaurant_service.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface RestaurantRepository extends
        JpaRepository<Restaurant, UUID>,
        JpaSpecificationExecutor<Restaurant>
{
    List<Restaurant> findByOwnerId(UUID ownerId);
}
