package com.food_delivery.restaurant_service.service;

import com.food_delivery.restaurant_service.dto.MenuItemRequestDto;
import com.food_delivery.restaurant_service.dto.MenuItemResponseDto;
import com.food_delivery.restaurant_service.exception.MenuCategoryNotFoundException;
import com.food_delivery.restaurant_service.exception.MenuItemNotFoundException;
import com.food_delivery.restaurant_service.exception.RestaurantNotFoundException;
import com.food_delivery.restaurant_service.mapper.MenuItemMapper;
import com.food_delivery.restaurant_service.model.MenuCategory;
import com.food_delivery.restaurant_service.model.MenuItem;
import com.food_delivery.restaurant_service.model.Restaurant;
import com.food_delivery.restaurant_service.repository.MenuCategoryRepository;
import com.food_delivery.restaurant_service.repository.MenuItemRepository;
import com.food_delivery.restaurant_service.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenuItemServiceTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private MenuCategoryRepository menuCategoryRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private MenuItemMapper menuItemMapper;

    @InjectMocks
    private MenuItemService menuItemService;

    private UUID menuItemId;
    private UUID menuCategoryId;
    private UUID restaurantId;

    private MenuItem menuItem;
    private MenuCategory menuCategory;
    private Restaurant restaurant;

    private MenuItemRequestDto requestDto;
    private MenuItemResponseDto responseDto;

    @BeforeEach
    void setUp() {
        menuItemId = UUID.randomUUID();

        menuCategoryId = UUID.randomUUID();
        menuCategory = new MenuCategory();
        ReflectionTestUtils.setField(menuCategory, "id", menuCategoryId);

        restaurantId = UUID.randomUUID();
        restaurant = new Restaurant();
        ReflectionTestUtils.setField(restaurant, "id", restaurantId);

        menuItem = new MenuItem();
        ReflectionTestUtils.setField(menuItem, "id", menuItemId);
        menuItem.setName("Prosciutto Crudo");
        menuItem.setDescription("Lorem ipsum");
        menuItem.setPrice(BigDecimal.valueOf(32.50));
        menuItem.setAvailable(true);
        menuItem.setImageUrl("crudo.jpg");
        menuItem.setCategory(menuCategory);
        menuItem.setRestaurant(restaurant);

        requestDto = new MenuItemRequestDto(
                "Prosciutto Crudo",
                "Lorem ipsum",
                BigDecimal.valueOf(32.50),
                true,
                "crudo.jpg",
                menuCategoryId,
                restaurantId
        );

        responseDto = new MenuItemResponseDto(
                menuItemId.toString(),
                "Prosciutto Crudo",
                "Lorem ipsum",
                BigDecimal.valueOf(32.50),
                "crudo.jpg",
                true
        );
    }

    @Test
    void getMenuItems_whenExists_shouldReturnListOfDtos() {
        when(menuItemRepository.findAll()).thenReturn(List.of(menuItem));
        when(menuItemMapper.toDto(any(MenuItem.class))).thenReturn(responseDto);

        List<MenuItemResponseDto> result = menuItemService.getMenuItems();

        assertThat(result).containsExactly(responseDto);
    }

    @Test
    void getMenuItemById_whenExists_shouldReturnDto() {
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(menuItem));
        when(menuItemMapper.toDto(any(MenuItem.class))).thenReturn(responseDto);

        MenuItemResponseDto result = menuItemService.getMenuItemById(menuItemId);

        assertThat(result).isEqualTo(responseDto);
    }

    @Test
    void getMenuItemById_whenNotFound_shouldThrown() {
        UUID missingId = UUID.randomUUID();

        when(menuItemRepository.findById(missingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> menuItemService.getMenuItemById(missingId))
                .isInstanceOf(MenuItemNotFoundException.class);

        verify(menuItemMapper, never()).toDto(any());
    }

    @Test
    void getMenuItemsByRestaurantId_whenExist_shouldReturnList() {
        when(menuItemRepository.findByRestaurantId(restaurantId)).thenReturn(List.of(menuItem));
        when(menuItemMapper.toDto(any(MenuItem.class))).thenReturn(responseDto);

        List<MenuItemResponseDto> result = menuItemService.getMenuItemsByRestaurantId(restaurantId);

        assertThat(result).containsExactly(responseDto);
    }

    @Test
    void getMenuItemsByRestaurantIdAndAvailable_whenExists_shouldReturnList() {
        when(menuItemRepository.findByRestaurantIdAndAvailableTrue(restaurantId)).thenReturn(List.of(menuItem));
        when(menuItemMapper.toDto(any(MenuItem.class))).thenReturn(responseDto);

        List<MenuItemResponseDto> result = menuItemService.getMenuItemByRestaurantIdAndAvailable(restaurantId);

        assertThat(result).containsExactly(responseDto);
    }

    @Test
    void getMenuItemsByCategoryId_whenExists_shouldReturnList() {
        when(menuItemRepository.findByCategoryId(menuCategoryId)).thenReturn(List.of(menuItem));
        when(menuItemMapper.toDto(any(MenuItem.class))).thenReturn(responseDto);

        List<MenuItemResponseDto> result = menuItemService.getMenuItemByCategoryId(menuCategoryId);

        assertThat(result).containsExactly(responseDto);
    }

    @Test
    void createMenuItem_shouldSaveAndReturnDto() {
        when(menuCategoryRepository.findById(menuCategoryId)).thenReturn(Optional.of(menuCategory));
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(menuItemMapper.toEntity(requestDto, menuCategory, restaurant)).thenReturn(menuItem);
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(menuItem);
        when(menuItemMapper.toDto(menuItem)).thenReturn(responseDto);

        MenuItemResponseDto result = menuItemService.createMenuItem(requestDto);

        assertThat(result).isEqualTo(responseDto);
    }

    @Test
    void createMenuItem_whenNoRestaurant_shouldThrown() {
        when(menuCategoryRepository.findById(menuCategoryId)).thenReturn(Optional.of(menuCategory));
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> menuItemService.createMenuItem(requestDto))
                .isInstanceOf(RestaurantNotFoundException.class);

        verify(menuItemMapper, never()).toEntity(any(), any(), any());
    }

    @Test
    void createMenuItem_whenNoCategory_shouldThrown() {
        when(menuCategoryRepository.findById(menuCategoryId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> menuItemService.createMenuItem(requestDto))
                .isInstanceOf(MenuCategoryNotFoundException.class);

        verify(restaurantRepository, never()).findById(any());
    }

    @Test
    void updateMenuItem_shouldSaveAndReturnDto() {
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(menuItem));
        when(menuCategoryRepository.findById(menuCategoryId)).thenReturn(Optional.of(menuCategory));
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(menuItem);
        when(menuItemMapper.toDto(menuItem)).thenReturn(responseDto);

        MenuItemResponseDto result = menuItemService.updateMenuItem(menuItemId, requestDto);

        assertThat(result).isEqualTo(responseDto);
    }

    @Test
    void updateMenuItem_whenNoMenuItem_shouldThrown(){
        UUID missingId = UUID.randomUUID();

        when(menuItemRepository.findById(missingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> menuItemService.updateMenuItem(missingId, requestDto))
                .isInstanceOf(MenuItemNotFoundException.class);

        verify(menuCategoryRepository, never()).findById(any());
    }

    @Test
    void updateMenuItem_whenNoMenuCategory_shouldThrown(){
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(menuItem));
        when(menuCategoryRepository.findById(menuCategoryId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> menuItemService.updateMenuItem(menuItemId, requestDto))
                .isInstanceOf(MenuCategoryNotFoundException.class);

        verify(restaurantRepository, never()).findById(any());
    }

    @Test
    void updateMenuItem_whenNoRestaurant_shouldThrown(){
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(menuItem));
        when(menuCategoryRepository.findById(menuCategoryId)).thenReturn(Optional.of(menuCategory));
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> menuItemService.updateMenuItem(menuItemId, requestDto))
                .isInstanceOf(RestaurantNotFoundException.class);

        verify(menuItemRepository, never()).save(any());
    }

    @Test
    void deleteMenuItem_whenExists_shouldDelete(){
        when(menuItemRepository.existsById(menuItemId)).thenReturn(true);

        menuItemService.deleteMenuItem(menuItemId);

        verify(menuItemRepository).deleteById(menuItemId);
    }

    @Test
    void deleteMenuItem_whenNotExists_shouldThrown(){
        UUID missingId = UUID.randomUUID();

        when(menuItemRepository.existsById(missingId)).thenReturn(false);

        assertThatThrownBy(() -> menuItemService.deleteMenuItem(missingId))
                .isInstanceOf(MenuItemNotFoundException.class);

        verify(menuItemRepository, never()).deleteById(any());
    }
}
