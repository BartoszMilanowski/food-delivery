package com.food_delivery.restaurant_service.controller;

import com.food_delivery.restaurant_service.dto.MenuCategoryRequestDto;
import com.food_delivery.restaurant_service.dto.MenuCategoryResponseDto;
import com.food_delivery.restaurant_service.service.MenuCategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/menu/categories")
public class MenuCategoryController {

    private final MenuCategoryService menuCategoryService;

    public MenuCategoryController(MenuCategoryService menuCategoryService){
        this.menuCategoryService = menuCategoryService;
    }


    @GetMapping
    public ResponseEntity<List<MenuCategoryResponseDto>> getMenuCategories(){
        return ResponseEntity.ok(menuCategoryService.getMenuCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuCategoryResponseDto> getMenuCategoryById(@PathVariable UUID id){
        return ResponseEntity.ok(menuCategoryService.getMenuCategoryById(id));
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<MenuCategoryResponseDto>> getMenuCategoriesByRestaurantId(@PathVariable UUID restaurantId){
        return ResponseEntity.ok(menuCategoryService.getMenuCategoriesByRestaurantId(restaurantId));
    }

    @PostMapping
    public ResponseEntity<MenuCategoryResponseDto> createMenuCategory(
            @Valid @RequestBody MenuCategoryRequestDto requestDto
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(menuCategoryService.createMenuCategory(requestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuCategoryResponseDto> updateMenuCategory(
            @PathVariable UUID id,
            @Valid @RequestBody MenuCategoryRequestDto requestDto
    ){
        return ResponseEntity.ok(menuCategoryService.updateMenuCategory(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuCategory(@PathVariable UUID id){
        menuCategoryService.deleteMenuCategory(id);
        return ResponseEntity.noContent().build();
    }


}
