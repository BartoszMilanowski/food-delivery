package com.food_delivery.restaurant_service.service;

import com.food_delivery.restaurant_service.dto.RestaurantRequestDto;
import com.food_delivery.restaurant_service.dto.RestaurantResponseDto;
import com.food_delivery.restaurant_service.exception.RestaurantNotFoundException;
import com.food_delivery.restaurant_service.mapper.RestaurantMapper;
import com.food_delivery.restaurant_service.model.Restaurant;
import com.food_delivery.restaurant_service.repository.RestaurantRepository;
import com.food_delivery.restaurant_service.specification.RestaurantSpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    public RestaurantService(RestaurantRepository restaurantRepository, RestaurantMapper restaurantMapper) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantMapper = restaurantMapper;
    }

    @Transactional
    public RestaurantResponseDto createRestaurant(RestaurantRequestDto dto) {
        Restaurant restaurant = restaurantMapper.toEntity(dto);
        Restaurant saved = restaurantRepository.save(restaurant);
        return restaurantMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public RestaurantResponseDto getRestaurantById(UUID id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));
        return restaurantMapper.toDto(restaurant);
    }

    @Transactional(readOnly = true)
    public List<RestaurantResponseDto> searchRestaurant(String city, String cuisineType, boolean active) {
        Specification<Restaurant> spec = Specification
                .where(RestaurantSpecifications.hasCity(city))
                .and(RestaurantSpecifications.hasCuisineType(cuisineType))
                .and(RestaurantSpecifications.isActive(active));

        return restaurantRepository.findAll(spec).stream()
                .map(restaurantMapper::toDto)
                .toList();
    }

    @Transactional
    public RestaurantResponseDto updateRestaurant(UUID id, RestaurantRequestDto dto) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));

        restaurantMapper.updateEntity(restaurant, dto);
        Restaurant updated = restaurantRepository.save(restaurant);

        return restaurantMapper.toDto(updated);
    }

    @Transactional
    public void deleteRestaurant(UUID id) {
        if (!restaurantRepository.existsById(id)) {
            throw new RestaurantNotFoundException(id);
        }
        restaurantRepository.deleteById(id);
    }
}
