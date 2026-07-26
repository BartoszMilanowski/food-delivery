package com.food_delivery.restaurant_service.service;

import com.food_delivery.restaurant_service.dto.RestaurantRequestDto;
import com.food_delivery.restaurant_service.dto.RestaurantResponseDto;
import com.food_delivery.restaurant_service.exception.RestaurantNotFoundException;
import com.food_delivery.restaurant_service.mapper.RestaurantMapper;
import com.food_delivery.restaurant_service.model.Restaurant;
import com.food_delivery.restaurant_service.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RestaurantMapper restaurantMapper;

    @InjectMocks
    private RestaurantService restaurantService;

    private UUID restaurantId;
    private Restaurant restaurant;
    private RestaurantRequestDto requestDto;
    private RestaurantResponseDto responseDto;

    @BeforeEach
    void setUp(){
        restaurantId = UUID.randomUUID();

        restaurant = new Restaurant();
        ReflectionTestUtils.setField(restaurant, "id", restaurantId);
        restaurant.setName("Pizza Napoli");

        requestDto = new RestaurantRequestDto(
                "Pizza Napoli", "Italian", "Kwiatowa 1", "Warszawa",
                "00-001", "123456789", true, UUID.randomUUID()
        );

        responseDto = new RestaurantResponseDto();
        responseDto.setId(restaurantId.toString());
        responseDto.setName("Pizza Napoli");
    }

    @Test
    void createRestaurant_shouldSaveAndReturnDto(){

        when(restaurantMapper.toEntity(requestDto)).thenReturn(restaurant);
        when(restaurantRepository.save(restaurant)).thenReturn(restaurant);
        when(restaurantMapper.toDto(restaurant)).thenReturn(responseDto);

        RestaurantResponseDto result = restaurantService.createRestaurant(requestDto);

        assertThat(result).isEqualTo(responseDto);
        verify(restaurantRepository).save(restaurant);
    }

    @Test
    void getRestaurantById_whenExists_shouldReturnDto(){
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(restaurantMapper.toDto(restaurant)).thenReturn(responseDto);

        RestaurantResponseDto result = restaurantService.getRestaurantById(restaurantId);

        assertThat(result).isEqualTo(responseDto);
    }

    @Test
    void getRestaurantById_whenNotFound_shouldThrow() {
        UUID missingId = UUID.randomUUID();
        when(restaurantRepository.findById(missingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> restaurantService.getRestaurantById(missingId))
                .isInstanceOf(RestaurantNotFoundException.class);

        verify(restaurantMapper, never()).toDto(any());
    }


    @Test
    void updateRestaurant_whenExists_shouldUpdateAndReturnDto() {
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(restaurant)).thenReturn(restaurant);
        when(restaurantMapper.toDto(restaurant)).thenReturn(responseDto);

        RestaurantResponseDto result = restaurantService.updateRestaurant(restaurantId, requestDto);

        assertThat(result).isEqualTo(responseDto);
        verify(restaurantMapper).updateEntity(restaurant, requestDto);
    }

    @Test
    void updateRestaurant_whenNotFound_shouldThrowAndNotSave() {
        UUID missingId = UUID.randomUUID();
        when(restaurantRepository.findById(missingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> restaurantService.updateRestaurant(missingId, requestDto))
                .isInstanceOf(RestaurantNotFoundException.class);

        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void deleteRestaurant_whenExists_shouldDelete() {
        when(restaurantRepository.existsById(restaurantId)).thenReturn(true);

        restaurantService.deleteRestaurant(restaurantId);

        verify(restaurantRepository).deleteById(restaurantId);
    }

    @Test
    void deleteRestaurant_whenNotFound_shouldThrowAndNotDelete() {
        UUID missingId = UUID.randomUUID();
        when(restaurantRepository.existsById(missingId)).thenReturn(false);

        assertThatThrownBy(() -> restaurantService.deleteRestaurant(missingId))
                .isInstanceOf(RestaurantNotFoundException.class);

        verify(restaurantRepository, never()).deleteById(any());
    }

    @Test
    void searchRestaurants_shouldDelegateToRepositoryAndMapResults() {
        when(restaurantRepository.findAll(ArgumentMatchers.<Specification<Restaurant>>any()))
                .thenReturn(List.of(restaurant));
        when(restaurantMapper.toDto(restaurant)).thenReturn(responseDto);

        List<RestaurantResponseDto> result = restaurantService.searchRestaurants("Warszawa", null, true, null, null);

        assertThat(result).containsExactly(responseDto);
    }
}
