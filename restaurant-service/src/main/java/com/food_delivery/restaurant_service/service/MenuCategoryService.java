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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class MenuCategoryService {

    private final MenuCategoryRepository menuCategoryRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuCategoryMapper menuCategoryMapper;

    public MenuCategoryService(MenuCategoryRepository menuCategoryRepository,
                               RestaurantRepository restaurantRepository,
                               MenuCategoryMapper menuCategoryMapper) {
        this.menuCategoryRepository = menuCategoryRepository;
        this.restaurantRepository = restaurantRepository;
        this.menuCategoryMapper = menuCategoryMapper;
    }

    @Transactional(readOnly = true)
    public List<MenuCategoryResponseDto> getMenuCategories() {
        return menuCategoryRepository.findAll()
                .stream()
                .map(menuCategoryMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public MenuCategoryResponseDto getMenuCategoryById(UUID id) {
        MenuCategory category = menuCategoryRepository.findById(id)
                .orElseThrow(() -> new MenuCategoryNotFoundException(id));

        return menuCategoryMapper.toDto(category);
    }

    @Transactional(readOnly = true)
    public List<MenuCategoryResponseDto> getMenuCategoriesByRestaurantId(UUID restaurantId) {
        return menuCategoryRepository.findByRestaurantIdOrderByDisplayOrderAsc(restaurantId)
                .stream()
                .map(menuCategoryMapper::toDto)
                .toList();
    }

    @Transactional
    public MenuCategoryResponseDto createMenuCategory(MenuCategoryRequestDto requestDto) {
        Restaurant restaurant = restaurantRepository.findById(requestDto.getRestaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException(requestDto.getRestaurantId()));

        int displayOrder = requestDto.getDisplayOrder() != null
                ? requestDto.getDisplayOrder()
                : nextDisplayOrder(restaurant.getId());

        MenuCategory menuCategory = new MenuCategory(requestDto.getName(), displayOrder, restaurant);
        MenuCategory saved = menuCategoryRepository.save(menuCategory);

        return menuCategoryMapper.toDto(saved);
    }

    @Transactional
    public MenuCategoryResponseDto updateMenuCategory(UUID id, MenuCategoryRequestDto requestDto) {
        MenuCategory menuCategory = menuCategoryRepository.findById(id)
                .orElseThrow(() -> new MenuCategoryNotFoundException(id));

        Restaurant restaurant = restaurantRepository.findById(requestDto.getRestaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException(requestDto.getRestaurantId()));

        int displayOrder = requestDto.getDisplayOrder() != null
                ? requestDto.getDisplayOrder()
                : menuCategory.getDisplayOrder();

        menuCategory.setName(requestDto.getName());
        menuCategory.setDisplayOrder(displayOrder);
        menuCategory.setRestaurant(restaurant);

        MenuCategory updated = menuCategoryRepository.save(menuCategory);

        return menuCategoryMapper.toDto(updated);
    }

    @Transactional
    public void deleteMenuCategory(UUID id) {
        if (!menuCategoryRepository.existsById(id)) {
            throw new MenuCategoryNotFoundException(id);
        }

        menuCategoryRepository.deleteById(id);
    }


    private int nextDisplayOrder(UUID restaurantId) {
        return menuCategoryRepository.findByRestaurantIdOrderByDisplayOrderAsc(restaurantId).stream()
                .mapToInt(MenuCategory::getDisplayOrder)
                .max()
                .orElse(-1) + 1;
    }
}
