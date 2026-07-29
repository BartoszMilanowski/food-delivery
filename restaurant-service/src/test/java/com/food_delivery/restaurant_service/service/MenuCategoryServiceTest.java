package com.food_delivery.restaurant_service.service;

import com.food_delivery.restaurant_service.dto.MenuCategoryRequestDto;
import com.food_delivery.restaurant_service.dto.MenuCategoryResponseDto;
import com.food_delivery.restaurant_service.exception.MenuCategoryNotFoundException;
import com.food_delivery.restaurant_service.exception.RestaurantNotFoundException;
import com.food_delivery.restaurant_service.mapper.MenuCategoryMapper;
import com.food_delivery.restaurant_service.model.MenuCategory;
import com.food_delivery.restaurant_service.model.Restaurant;
import com.food_delivery.restaurant_service.repository.MenuCategoryRepository;
import com.food_delivery.restaurant_service.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenuCategoryServiceTest {


    @Mock
    private MenuCategoryRepository menuCategoryRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private MenuCategoryMapper menuCategoryMapper;

    @InjectMocks
    private MenuCategoryService menuCategoryService;

    private UUID menuCategoryId;
    private MenuCategory menuCategory;
    private MenuCategoryResponseDto responseDto;
    private MenuCategoryRequestDto requestDto;

    private UUID restaurantId;
    private Restaurant restaurant;

    @BeforeEach
    void setUp(){

        menuCategoryId = UUID.randomUUID();

        menuCategory = new MenuCategory();
        ReflectionTestUtils.setField(menuCategory, "id", menuCategoryId);
        menuCategory.setName("Pizza");
        menuCategory.setDisplayOrder(1);
        menuCategory.setRestaurant(restaurant);

        restaurantId = UUID.randomUUID();

        restaurant = new Restaurant();
        ReflectionTestUtils.setField(restaurant, "id", restaurantId);
        restaurant.setName("Pizza Napoli");

        requestDto = new MenuCategoryRequestDto("Pizza", 1, restaurantId);

        responseDto = new MenuCategoryResponseDto();
        responseDto.setId(menuCategoryId.toString());
        responseDto.setName("Pizza");
    }

    @Test
    void getMenuCategoryById_whenExists_shouldReturnDto(){

        when(menuCategoryRepository.findById(menuCategoryId)).thenReturn(Optional.of(menuCategory));
        when(menuCategoryMapper.toDto(menuCategory)).thenReturn(responseDto);

        MenuCategoryResponseDto result = menuCategoryService.getMenuCategoryById(menuCategoryId);

        assertThat(result).isEqualTo(responseDto);
    }

    @Test
    void getMenuCategoryById_whenNotExists_shouldThrow(){
        UUID missingId = UUID.randomUUID();

        when(menuCategoryRepository.findById(missingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> menuCategoryService.getMenuCategoryById(missingId))
                .isInstanceOf(MenuCategoryNotFoundException.class);

        verify(menuCategoryMapper, never()).toDto(any());
    }

    @Test
    void deleteMenuCategory_whenExists_shouldDelete(){

        when(menuCategoryRepository.existsById(menuCategoryId)).thenReturn(true);

        menuCategoryService.deleteMenuCategory(menuCategoryId);

        verify(menuCategoryRepository).deleteById(menuCategoryId);
    }

    @Test
    void deleteMenuCategory_whenNotFound_shouldThrown(){
        UUID missingId = UUID.randomUUID();

        when(menuCategoryRepository.existsById(missingId)).thenReturn(false);

        assertThatThrownBy(() -> menuCategoryService.deleteMenuCategory(missingId))
                .isInstanceOf(MenuCategoryNotFoundException.class);

        verify(menuCategoryRepository, never()).deleteById(any());
    }

    @Test
    void getMenuCategoriesByRestaurantId_whenExists_shouldReturnListOfDtos(){
        when(menuCategoryRepository.findByRestaurantIdOrderByDisplayOrderAsc(restaurantId)).thenReturn(List.of(menuCategory));
        when(menuCategoryMapper.toDto(menuCategory)).thenReturn(responseDto);

        List<MenuCategoryResponseDto> result = menuCategoryService.getMenuCategoriesByRestaurantId(restaurantId);

        assertThat(result).containsExactly(responseDto);
    }

    @Test
    void getMenuCategoriesByRestaurantId_whenNoCategories_shouldReturnEmptyList(){
        when(menuCategoryRepository.findByRestaurantIdOrderByDisplayOrderAsc(restaurantId)).thenReturn(List.of());

        List<MenuCategoryResponseDto> result = menuCategoryService.getMenuCategoriesByRestaurantId(restaurantId);

        assertThat(result).isEmpty();
        verify(menuCategoryMapper, never()).toDto(any());
    }

    @Test
    void createMenuCategory_shouldSaveAndReturnDto(){
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(menuCategoryMapper.toEntity(requestDto, restaurant, requestDto.getDisplayOrder())).thenReturn(menuCategory);
        when(menuCategoryRepository.save(any(MenuCategory.class))).thenReturn(menuCategory);
        when(menuCategoryMapper.toDto(menuCategory)).thenReturn(responseDto);

        MenuCategoryResponseDto result = menuCategoryService.createMenuCategory(requestDto);

        assertThat(result).isEqualTo(responseDto);
    }

    @Test
    void createMenuCategory_whenDisplayOrderIsNull_shouldCalculateNextAndReturnDto() {
        MenuCategoryRequestDto requestWithNullOrder = new MenuCategoryRequestDto();
        requestWithNullOrder.setName("Pizza");
        requestWithNullOrder.setRestaurantId(restaurantId);
        requestWithNullOrder.setDisplayOrder(null);

        MenuCategory existingCategory = new MenuCategory();
        existingCategory.setName("Pasta");
        existingCategory.setDisplayOrder(2);
        existingCategory.setRestaurant(restaurant);

        MenuCategory savedCategory = new MenuCategory();
        savedCategory.setName("Pizza");
        savedCategory.setDisplayOrder(3);
        savedCategory.setRestaurant(restaurant);

        MenuCategoryResponseDto expectedDto = new MenuCategoryResponseDto();
        expectedDto.setName("Pizza");
        expectedDto.setDisplayOrder(3);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(menuCategoryRepository.findByRestaurantIdOrderByDisplayOrderAsc(restaurantId))
                .thenReturn(List.of(existingCategory));
        when(menuCategoryMapper.toEntity(requestWithNullOrder, restaurant, 3)).thenReturn(savedCategory);
        when(menuCategoryRepository.save(any(MenuCategory.class))).thenReturn(savedCategory);
        when(menuCategoryMapper.toDto(savedCategory)).thenReturn(expectedDto);

        MenuCategoryResponseDto result = menuCategoryService.createMenuCategory(requestWithNullOrder);

        assertThat(result).isEqualTo(expectedDto);
        verify(menuCategoryMapper).toEntity(requestWithNullOrder, restaurant, 3);
    }


    @Test
    void createMenuCategory_whenNoRestaurant_shouldThrown(){
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> menuCategoryService.createMenuCategory(requestDto))
                .isInstanceOf(RestaurantNotFoundException.class);

        verify(menuCategoryRepository, never()).save(any());
    }

    @Test
    void updateMenuCategory_shouldSaveAndReturnDto(){
        when(menuCategoryRepository.findById(menuCategoryId)).thenReturn(Optional.of(menuCategory));
        when(restaurantRepository.findById(requestDto.getRestaurantId())).thenReturn(Optional.of(restaurant));
        when(menuCategoryRepository.save(any(MenuCategory.class))).thenReturn(menuCategory);
        when(menuCategoryMapper.toDto(menuCategory)).thenReturn(responseDto);

        MenuCategoryResponseDto result = menuCategoryService.updateMenuCategory(menuCategoryId, requestDto);

        assertThat(result).isEqualTo(responseDto);
        verify(menuCategoryMapper).toDto(any());
    }

    @Test
    void updateMenuCategory_whenNotFound_shouldThrown(){
        UUID missingId = UUID.randomUUID();

        when(menuCategoryRepository.findById(missingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> menuCategoryService.updateMenuCategory(missingId, requestDto))
                .isInstanceOf(MenuCategoryNotFoundException.class);

        verify(menuCategoryMapper, never()).toDto(any());
        verify(restaurantRepository, never()).findById(any());

    }

    @Test
    void updateMenuCategory_whenRestaurantNotFound_shouldThrow() {
        when(menuCategoryRepository.findById(menuCategoryId)).thenReturn(Optional.of(menuCategory));
        when(restaurantRepository.findById(requestDto.getRestaurantId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> menuCategoryService.updateMenuCategory(menuCategoryId, requestDto))
                .isInstanceOf(RestaurantNotFoundException.class);

        verify(menuCategoryRepository, never()).save(any());
    }

    @Test
    void updateMenuCategory_whenDisplayOrderIsNull_shouldPreserveExisting() {
        MenuCategoryRequestDto requestWithoutOrder = new MenuCategoryRequestDto();
        requestWithoutOrder.setName("Nowa nazwa");
        requestWithoutOrder.setRestaurantId(restaurantId);
        requestWithoutOrder.setDisplayOrder(null);

        menuCategory.setDisplayOrder(5);

        when(menuCategoryRepository.findById(menuCategoryId)).thenReturn(Optional.of(menuCategory));
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(menuCategoryRepository.save(any(MenuCategory.class))).thenReturn(menuCategory);
        when(menuCategoryMapper.toDto(menuCategory)).thenReturn(responseDto);

        menuCategoryService.updateMenuCategory(menuCategoryId, requestWithoutOrder);

        assertThat(menuCategory.getDisplayOrder()).isEqualTo(5);
        verify(menuCategoryRepository, never()).findByRestaurantIdOrderByDisplayOrderAsc(any());
    }
}
