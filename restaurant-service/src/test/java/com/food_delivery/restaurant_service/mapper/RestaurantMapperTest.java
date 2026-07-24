package com.food_delivery.restaurant_service.mapper;

import com.food_delivery.restaurant_service.dto.RestaurantRequestDto;
import com.food_delivery.restaurant_service.dto.RestaurantResponseDto;
import com.food_delivery.restaurant_service.model.Restaurant;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RestaurantMapperTest {

    private final RestaurantMapper mapper = new RestaurantMapper();

    @Test
    void toDto_shouldMapAllFields(){
        UUID id = UUID.randomUUID();
        Restaurant restaurant = new Restaurant();
        ReflectionTestUtils.setField(restaurant, "id", id);
        restaurant.setName("Pizza Napoli");
        restaurant.setCuisineType("Italian");
        restaurant.setStreet("Jana Pawła II");
        restaurant.setCity("Warszawa");
        restaurant.setPostalCode("00-001");
        restaurant.setPhoneNumber("123456789");

        RestaurantResponseDto dto = mapper.toDto(restaurant);

        assertThat(dto.getId().toString()).isEqualTo(id.toString());
        assertThat(dto.getName()).isEqualTo("Pizza Napoli");
        assertThat(dto.getCuisineType()).isEqualTo("Italian");
        assertThat(dto.getStreet()).isEqualTo("Jana Pawła II");
        assertThat(dto.getCity()).isEqualTo("Warszawa");
        assertThat(dto.getPostalCode()).isEqualTo("00-001");
        assertThat(dto.getPhoneNumber()).isEqualTo("123456789");
    }

    @Test
    void toEntity_shouldMapAllFields(){
        UUID ownerId = UUID.randomUUID();

        RestaurantRequestDto dto = new RestaurantRequestDto();
        dto.setName("Pizza Napoli");
        dto.setCuisineType("Italian");
        dto.setStreet("Jana Pawła II");
        dto.setCity("Warszawa");
        dto.setPostalCode("00-001");
        dto.setPhoneNumber("123456789");
        dto.setActive(true);
        dto.setOwnerId(ownerId);

        Restaurant restaurant = mapper.toEntity(dto);

        assertThat(restaurant.getName()).isEqualTo("Pizza Napoli");
        assertThat(restaurant.getCuisineType()).isEqualTo("Italian");
        assertThat(restaurant.getStreet()).isEqualTo("Jana Pawła II");
        assertThat(restaurant.getCity()).isEqualTo("Warszawa");
        assertThat(restaurant.getPostalCode()).isEqualTo("00-001");
        assertThat(restaurant.getPhoneNumber()).isEqualTo("123456789");
        assertThat(restaurant.isActive());
        assertThat(restaurant.getOwnerId()).isEqualTo(ownerId);
    }
}
