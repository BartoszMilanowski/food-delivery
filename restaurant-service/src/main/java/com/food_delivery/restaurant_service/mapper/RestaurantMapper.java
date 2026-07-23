package com.food_delivery.restaurant_service.mapper;

import com.food_delivery.restaurant_service.dto.RestaurantRequestDto;
import com.food_delivery.restaurant_service.dto.RestaurantResponseDto;
import com.food_delivery.restaurant_service.model.Restaurant;
import org.springframework.stereotype.Component;

@Component
public class RestaurantMapper {

    public RestaurantResponseDto toDto(Restaurant r) {

        RestaurantResponseDto dto = new RestaurantResponseDto();
        dto.setId(r.getId().toString());
        dto.setName(r.getName());
        dto.setCuisineType(r.getCuisineType());
        dto.setStreet(r.getStreet());
        dto.setCity(r.getCity());
        dto.setPostalCode(r.getPostalCode());
        dto.setPhoneNumber(r.getPhoneNumber());

        return dto;
    }

    public Restaurant toEntity(RestaurantRequestDto dto) {

        Restaurant r = new Restaurant();

        r.setName(dto.getName());
        r.setCuisineType(dto.getCuisineType());
        r.setStreet(dto.getStreet());
        r.setCity(dto.getCity());
        r.setPostalCode(dto.getPostalCode());
        r.setPhoneNumber(dto.getPhoneNumber());
        r.setActive(dto.isActive());
        r.setOwnerId(dto.getOwnerId());

        return r;
    }

    public void updateEntity(Restaurant r, RestaurantRequestDto dto){
        r.setName(dto.getName());
        r.setCuisineType(dto.getCuisineType());
        r.setStreet(dto.getStreet());
        r.setCity(dto.getCity());
        r.setPostalCode(dto.getPostalCode());
        r.setPhoneNumber(dto.getPhoneNumber());
        r.setActive(dto.isActive());
    }
}
