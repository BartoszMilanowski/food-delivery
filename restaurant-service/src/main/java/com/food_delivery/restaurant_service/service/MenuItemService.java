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
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final MenuItemMapper menuItemMapper;
    private final MenuCategoryRepository menuCategoryRepository;
    private final RestaurantRepository restaurantRepository;

    public MenuItemService(MenuItemRepository menuItemRepository,
                           MenuItemMapper menuItemMapper,
                           MenuCategoryRepository menuCategoryRepository,
                           RestaurantRepository restaurantRepository) {
        this.menuItemRepository = menuItemRepository;
        this.menuItemMapper = menuItemMapper;
        this.menuCategoryRepository = menuCategoryRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional(readOnly = true)
    public List<MenuItemResponseDto> getMenuItems() {
        return menuItemRepository.findAll()
                .stream()
                .map(menuItemMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public MenuItemResponseDto getMenuItemById(UUID id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new MenuItemNotFoundException(id));

        return menuItemMapper.toDto(menuItem);
    }

    @Transactional(readOnly = true)
    public List<MenuItemResponseDto> getMenuItemsByRestaurantId(UUID restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(menuItemMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MenuItemResponseDto> getMenuItemByRestaurantIdAndAvailable(UUID restaurantId) {
        return menuItemRepository.findByRestaurantIdAndAvailableTrue(restaurantId)
                .stream()
                .map(menuItemMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MenuItemResponseDto> getMenuItemByCategoryId(UUID categoryId) {
        return menuItemRepository.findByCategoryId(categoryId)
                .stream()
                .map(menuItemMapper::toDto)
                .toList();
    }

    @Transactional
    public MenuItemResponseDto createMenuItem(MenuItemRequestDto requestDto) {
        MenuCategory menuCategory = menuCategoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new MenuCategoryNotFoundException(requestDto.getCategoryId()));

        Restaurant restaurant = restaurantRepository.findById(requestDto.getRestaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException(requestDto.getRestaurantId()));

        MenuItem menuItem = menuItemMapper.toEntity(requestDto, menuCategory, restaurant);
        MenuItem saved = menuItemRepository.save(menuItem);

        return menuItemMapper.toDto(menuItem);
    }

    @Transactional
    public MenuItemResponseDto updateMenuItem(UUID id, MenuItemRequestDto requestDto) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new MenuItemNotFoundException(id));

        MenuCategory menuCategory = menuCategoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new MenuCategoryNotFoundException(requestDto.getCategoryId()));

        Restaurant restaurant = restaurantRepository.findById(requestDto.getRestaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException(requestDto.getRestaurantId()));

        menuItem.setName(requestDto.getName());
        menuItem.setDescription(requestDto.getDescription());
        menuItem.setPrice(requestDto.getPrice());
        menuItem.setAvailable(requestDto.isAvailable());
        menuItem.setImageUrl(requestDto.getImageUrl());
        menuItem.setCategory(menuCategory);
        menuItem.setRestaurant(restaurant);

        MenuItem updated = menuItemRepository.save(menuItem);

        return menuItemMapper.toDto(updated);
    }

    @Transactional
    public void deleteMenuItem(UUID id) {
        if (!menuItemRepository.existsById(id)) {
            throw new MenuItemNotFoundException(id);
        }

        menuItemRepository.deleteById(id);
    }
}