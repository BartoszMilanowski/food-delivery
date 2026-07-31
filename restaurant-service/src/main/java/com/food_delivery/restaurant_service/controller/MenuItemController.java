package com.food_delivery.restaurant_service.controller;

import com.food_delivery.restaurant_service.dto.MenuItemRequestDto;
import com.food_delivery.restaurant_service.dto.MenuItemResponseDto;
import com.food_delivery.restaurant_service.service.MenuItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/menu/items")
public class MenuItemController {

    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @GetMapping
    public ResponseEntity<List<MenuItemResponseDto>> getMenuItems() {
        return ResponseEntity.ok(menuItemService.getMenuItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemResponseDto> getMenuItemById(@PathVariable UUID id) {
        return ResponseEntity.ok(menuItemService.getMenuItemById(id));
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<MenuItemResponseDto>> getMenuItemsByRestaurantId(
            @PathVariable UUID restaurantId
    ) {
        return ResponseEntity.ok(menuItemService.getMenuItemsByRestaurantId(restaurantId));
    }

    @GetMapping("/restaurant/available/{restaurantId}")
    public ResponseEntity<List<MenuItemResponseDto>> getMenuItemsByRestaurantAndAvailable(
            @PathVariable UUID restaurantId
    ) {
        return ResponseEntity.ok(menuItemService.getMenuItemByRestaurantIdAndAvailable(restaurantId));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<MenuItemResponseDto>> getMenuItemsByCategoryId(
            @PathVariable UUID categoryId
    ) {
        return ResponseEntity.ok(menuItemService.getMenuItemByCategoryId(categoryId));
    }

    @PostMapping
    public ResponseEntity<MenuItemResponseDto> createMenuItem(
            @Valid @RequestBody MenuItemRequestDto requestDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(menuItemService.createMenuItem(requestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItemResponseDto> updateMenuItem(
            @PathVariable UUID id,
            @Valid @RequestBody MenuItemRequestDto requestDto
    ) {
        return ResponseEntity.ok(menuItemService.updateMenuItem(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(
            @PathVariable UUID id
    ) {
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.noContent().build();
    }
}
