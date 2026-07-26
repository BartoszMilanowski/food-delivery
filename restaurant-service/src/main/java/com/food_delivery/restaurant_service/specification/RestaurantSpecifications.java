package com.food_delivery.restaurant_service.specification;

import com.food_delivery.restaurant_service.model.Restaurant;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class RestaurantSpecifications {

    private RestaurantSpecifications() {
    }

    public static Specification<Restaurant> hasCity(String city) {
        return (root, query, cb) ->
                city == null ? cb.conjunction() : cb.equal(cb.lower(root.get("city")), city.toLowerCase());
    }

    public static Specification<Restaurant> hasCuisineType(String cuisineType) {
        return (root, query, cb) ->
                cuisineType == null ? cb.conjunction() : cb.equal(cb.lower(root.get("cuisineType")), cuisineType.toLowerCase());
    }

    public static Specification<Restaurant> isActive(Boolean active) {
        return (root, query, cb) ->
                active == null ? cb.conjunction() : cb.equal(root.get("active"), active);
    }

    public static Specification<Restaurant> nameContains(String phrase) {
        return (root, query, cb) ->
                phrase == null
                        ? cb.conjunction()
                        : cb.like(cb.lower(root.get("name")), "%" + phrase.toLowerCase() + "%");
    }

    public static Specification<Restaurant> ownerIdEquals(UUID ownerId) {
        return (root, query, cb) ->
                ownerId == null ? cb.conjunction() : cb.equal(root.get("ownerId"), ownerId);
    }
}
