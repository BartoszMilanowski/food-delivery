package com.food_delivery.restaurant_service.mapper;

import com.food_delivery.restaurant_service.dto.MenuCategoryRequestDto;
import com.food_delivery.restaurant_service.dto.MenuCategoryResponseDto;
import com.food_delivery.restaurant_service.model.MenuCategory;
import com.food_delivery.restaurant_service.model.Restaurant;
import org.springframework.stereotype.Component;

@Component
public class MenuCategoryMapper {

    public MenuCategoryResponseDto toDto(MenuCategory mc) {
        MenuCategoryResponseDto dto = new MenuCategoryResponseDto();

        dto.setId(mc.getId().toString());
        dto.setName(mc.getName());
        dto.setDisplayOrder(mc.getDisplayOrder());

        return dto;
    }

    public MenuCategory toEntity(MenuCategoryRequestDto dto, Restaurant restaurant) {
        MenuCategory mc = new MenuCategory();

        mc.setName(dto.getName());
        mc.setDisplayOrder(dto.getDisplayOrder());
        mc.setRestaurant(restaurant);

        return mc;
    }
}
