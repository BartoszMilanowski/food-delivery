package com.food_delivery.restaurant_service.mapper;

import com.food_delivery.restaurant_service.dto.MenuCategoryRequestDto;
import com.food_delivery.restaurant_service.dto.MenuCategoryResponseDto;
import com.food_delivery.restaurant_service.model.MenuCategory;
import com.food_delivery.restaurant_service.model.Restaurant;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MenuCategoryMapperTest {

    private final MenuCategoryMapper menuCategoryMapper = new MenuCategoryMapper();

    @Test
    void toDto_shouldMapAllFields(){

        UUID menuCategoryId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();

        MenuCategory menuCategory = new MenuCategory();
        Restaurant restaurant = new Restaurant();
        ReflectionTestUtils.setField(restaurant, "id", restaurantId);

        ReflectionTestUtils.setField(menuCategory, "id", menuCategoryId);
        menuCategory.setName("Pizza");
        menuCategory.setDisplayOrder(3);
        menuCategory.setRestaurant(restaurant);

        MenuCategoryResponseDto result = menuCategoryMapper.toDto(menuCategory);

        assertThat(result.getId()).isEqualTo(menuCategoryId.toString());
        assertThat(result.getName()).isEqualTo("Pizza");
        assertThat(result.getDisplayOrder()).isEqualTo(3);
    }

    @Test
    void toEntity_shouldMapAllFields(){

        UUID restaurantId = UUID.randomUUID();

        MenuCategoryRequestDto requestDto = new MenuCategoryRequestDto();
        Restaurant restaurant = new Restaurant();
        ReflectionTestUtils.setField(restaurant, "id", restaurantId);

        requestDto.setName("Pizza");
        requestDto.setDisplayOrder(3);
        requestDto.setRestaurantId(restaurantId);

        MenuCategory result = menuCategoryMapper.toEntity(requestDto, restaurant, 3);

        assertThat(result.getName()).isEqualTo("Pizza");
        assertThat(result.getDisplayOrder()).isEqualTo(3);
        assertThat(result.getRestaurant()).isEqualTo(restaurant);

    }

}
