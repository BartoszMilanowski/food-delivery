package com.food_delivery.restaurant_service.controller;

import com.food_delivery.restaurant_service.dto.RestaurantRequestDto;
import com.food_delivery.restaurant_service.dto.RestaurantResponseDto;
import com.food_delivery.restaurant_service.service.RestaurantService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/restaurants")
@Tag(name = "Restaurant", description = "API for managing restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponseDto>> getRestaurants() {
        return ResponseEntity.ok(restaurantService.getRestaurants());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponseDto> getRestaurantById(@PathVariable UUID id) {
        return ResponseEntity.ok(restaurantService.getRestaurantById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<RestaurantResponseDto>> searchRestaurant(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String cuisineType,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) String name
    ) {
        return ResponseEntity.ok(restaurantService.searchRestaurants(city, cuisineType, active, name, null));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<RestaurantResponseDto>> searchRestaurantsByOwner(@PathVariable UUID ownerId) {
        return ResponseEntity.ok(restaurantService.searchRestaurants(null, null, null, null, ownerId));
    }

    @PostMapping
    public ResponseEntity<RestaurantResponseDto> createRestaurant(@Valid @RequestBody RestaurantRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantService.createRestaurant(requestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponseDto> updateRestaurant(@PathVariable UUID id, @Valid @RequestBody RestaurantRequestDto requestDto) {
        return ResponseEntity.ok().body(restaurantService.updateRestaurant(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable UUID id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }
}
