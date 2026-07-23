package com.food_delivery.restaurant_service.specification;

import com.food_delivery.restaurant_service.model.Restaurant;
import org.springframework.data.jpa.domain.Specification;

public class RestaurantSpecifications {

    private RestaurantSpecifications() {
    }

    public static Specification<Restaurant> hasCity(String city) {
        return (root, query, cb) ->
                city == null ? cb.conjunction() : cb.equal(root.get("city"), city);
    }

    public static Specification<Restaurant> hasCuisineType(String cuisineType) {
        return (root, query, cb) ->
                cuisineType == null ? cb.conjunction() : cb.equal(root.get("cuisineType"), cuisineType);
    }

    public static Specification<Restaurant> isActive(Boolean active) {
        return (root, query, cb) ->
                active == null ? cb.conjunction() : cb.equal(root.get("active"), active);
    }
}
