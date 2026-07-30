package com.food_delivery.restaurant_service.mapper;

import com.food_delivery.restaurant_service.dto.MenuItemRequestDto;
import com.food_delivery.restaurant_service.dto.MenuItemResponseDto;
import com.food_delivery.restaurant_service.model.MenuCategory;
import com.food_delivery.restaurant_service.model.MenuItem;
import com.food_delivery.restaurant_service.model.Restaurant;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MenuItemMapperTest {

    private final MenuItemMapper menuItemMapper = new MenuItemMapper();

    @Test
    void toDto_shouldMapAllFields(){

        UUID menuItemId = UUID.randomUUID();
        UUID menuCategoryId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();

        MenuItem menuItem = new MenuItem();

        MenuCategory menuCategory = new MenuCategory();
        ReflectionTestUtils.setField(menuCategory, "id", menuCategoryId);

        Restaurant restaurant = new Restaurant();
        ReflectionTestUtils.setField(restaurant, "id", restaurantId);

        ReflectionTestUtils.setField(menuItem, "id", menuItemId);
        menuItem.setName("Prosciutto Crudo");
        menuItem.setDescription("About prosciutto crudo");
        menuItem.setPrice(BigDecimal.valueOf(35.50));
        menuItem.setAvailable(true);
        menuItem.setImageUrl("crudo.jpg");
        menuItem.setCategory(menuCategory);
        menuItem.setRestaurant(restaurant);

        MenuItemResponseDto result = menuItemMapper.toDto(menuItem);

        assertThat(result.getId()).isEqualTo(menuItemId.toString());
        assertThat(result.getName()).isEqualTo("Prosciutto Crudo");
        assertThat(result.getDescription()).isEqualTo("About prosciutto crudo");
        assertThat(result.getPrice()).isEqualTo(BigDecimal.valueOf(35.50));
        assertThat(result.isAvailable());
        assertThat(result.getImageUrl()).isEqualTo("crudo.jpg");
    }

    @Test
    void toEntity_shouldMapAllFields(){

        UUID menuCategoryId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();

        MenuCategory menuCategory = new MenuCategory();
        ReflectionTestUtils.setField(menuCategory, "id", menuCategoryId);

        Restaurant restaurant = new Restaurant();
        ReflectionTestUtils.setField(restaurant, "id", restaurantId);

        MenuItemRequestDto requestDto = new MenuItemRequestDto();
        requestDto.setName("Prosciutto Crudo");
        requestDto.setDescription("About prosciutto crudo");
        requestDto.setPrice(BigDecimal.valueOf(35.50));
        requestDto.setAvailable(true);
        requestDto.setImageUrl("crudo.jpg");
        requestDto.setCategoryId(menuCategoryId);
        requestDto.setRestaurantId(restaurantId);

        MenuItem result = menuItemMapper.toEntity(requestDto, menuCategory, restaurant);

        assertThat(result.getName()).isEqualTo("Prosciutto Crudo");
        assertThat(result.getDescription()).isEqualTo("About prosciutto crudo");
        assertThat(result.getPrice()).isEqualTo(BigDecimal.valueOf(35.50));
        assertThat(result.isAvailable());
        assertThat(result.getImageUrl()).isEqualTo("crudo.jpg");
        assertThat(result.getCategory()).isEqualTo(menuCategory);
        assertThat(result.getRestaurant()).isEqualTo(restaurant);
    }
}
