package com.food_delivery.restaurant_service.mapper;

import com.food_delivery.restaurant_service.dto.MenuItemRequestDto;
import com.food_delivery.restaurant_service.dto.MenuItemResponseDto;
import com.food_delivery.restaurant_service.model.MenuCategory;
import com.food_delivery.restaurant_service.model.MenuItem;
import com.food_delivery.restaurant_service.model.Restaurant;
import org.springframework.stereotype.Component;

@Component
public class MenuItemMapper {

    public MenuItemResponseDto toDto(MenuItem mi){

        MenuItemResponseDto dto = new MenuItemResponseDto();

        dto.setId(mi.getId().toString());
        dto.setName(mi.getName());
        dto.setDescription(mi.getDescription());
        dto.setPrice(mi.getPrice());
        dto.setImageUrl(mi.getImageUrl());
        dto.setAvailable(mi.isAvailable());

        return dto;
    }

    public MenuItem toEntity(MenuItemRequestDto dto, MenuCategory category, Restaurant restaurant){
        MenuItem mi = new MenuItem();

        mi.setName(dto.getName());
        mi.setDescription(dto.getDescription());
        mi.setPrice(dto.getPrice());
        mi.setAvailable(dto.isAvailable());
        mi.setImageUrl(dto.getImageUrl());
        mi.setCategory(category);
        mi.setRestaurant(restaurant);

        return mi;
    }
}
