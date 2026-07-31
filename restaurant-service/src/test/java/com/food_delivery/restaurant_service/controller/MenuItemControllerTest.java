package com.food_delivery.restaurant_service.controller;

import com.food_delivery.restaurant_service.dto.MenuItemRequestDto;
import com.food_delivery.restaurant_service.dto.MenuItemResponseDto;
import com.food_delivery.restaurant_service.exception.MenuItemNotFoundException;
import com.food_delivery.restaurant_service.service.MenuItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuItemController.class)
public class MenuItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MenuItemService menuItemService;

    @Test
    void getMenuItems__shouldReturnList() throws Exception {
        MenuItemResponseDto dto = sampleResponseDto();

        when(menuItemService.getMenuItems()).thenReturn(List.of(dto));

        mockMvc.perform(get("/menu/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Prosciutto Crudo"));
    }

    @Test
    void getMenuItemById_whenExists_shouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();
        MenuItemResponseDto responseDto = sampleResponseDto();
        when(menuItemService.getMenuItemById(id)).thenReturn(responseDto);

        mockMvc.perform(get("/menu/items/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Prosciutto Crudo"));
    }

    @Test
    void getMenuItemById_whenNotFound_shouldReturn404() throws Exception {
        UUID id = UUID.randomUUID();
        when(menuItemService.getMenuItemById(id)).thenThrow(new MenuItemNotFoundException(id));

        mockMvc.perform(get("/menu/items/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void getMenuItemsByRestaurantId_whenExist_shouldReturnList() throws Exception {
        UUID restaurantId = UUID.randomUUID();
        MenuItemResponseDto responseDto = sampleResponseDto();
        when(menuItemService.getMenuItemsByRestaurantId(restaurantId)).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/menu/items/restaurant/{restaurantId}", restaurantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Prosciutto Crudo"));
    }

    @Test
    void getMenuItemsByRestaurantIdAndAvailable_shouldReturnList() throws Exception {
        UUID restaurantId = UUID.randomUUID();
        MenuItemResponseDto responseDto = sampleResponseDto();
        when(menuItemService.getMenuItemByRestaurantIdAndAvailable(restaurantId)).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/menu/items/restaurant/available/{restaurantId}", restaurantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Prosciutto Crudo"));
    }

    @Test
    void getMenuItemsByCategoryId_shouldReturnList() throws Exception {
        UUID categoryId = UUID.randomUUID();
        MenuItemResponseDto responseDto = sampleResponseDto();
        when(menuItemService.getMenuItemByCategoryId(categoryId)).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/menu/items/category/{categoryId}", categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Prosciutto Crudo"));
    }

    @Test
    void createMenuItem_withValidBody_shouldReturn200() throws Exception {
        MenuItemRequestDto requestDto = sampleRequestDto();
        MenuItemResponseDto responseDto = sampleResponseDto();
        when(menuItemService.createMenuItem(any(MenuItemRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/menu/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Prosciutto Crudo"));
    }

    @Test
    void createMenuItem_withBlankNameAndBlankDescription_shouldReturn400() throws Exception {
        MenuItemRequestDto invalidDto = sampleInvalidRequestDto();


        mockMvc.perform(post("/menu/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.name").value("Name is required"));
    }

    @Test
    void updateMenuItem_withValidBody_shouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();
        MenuItemRequestDto requestDto = sampleRequestDto();
        MenuItemResponseDto responseDto = sampleResponseDto();
        when(menuItemService.updateMenuItem(eq(id), any(MenuItemRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(put("/menu/items/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Prosciutto Crudo"));
    }

    @Test
    void updateMenuItem_withBlankNameAndDescription_shouldReturn400() throws Exception {
        UUID id = UUID.randomUUID();
        MenuItemRequestDto invalidDto = sampleInvalidRequestDto();

        mockMvc.perform(put("/menu/items/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.name").value("Name is required"));
    }

    @Test
    void deleteMenuItem_whenExists_shouldReturn200() throws Exception{
        UUID id = UUID.randomUUID();
        doNothing().when(menuItemService).deleteMenuItem(id);

        mockMvc.perform(delete("/menu/items/{id}", id))
                .andExpect(status().isNoContent());

        verify(menuItemService).deleteMenuItem(id);
    }

    private MenuItemRequestDto sampleRequestDto() {
        return new MenuItemRequestDto(
                "Prosciutto Crudo",
                "Lorem ipsum",
                BigDecimal.valueOf(32.50),
                true,
                "crudo.jpg",
                UUID.randomUUID(),
                UUID.randomUUID()
        );
    }

    private MenuItemResponseDto sampleResponseDto() {
        return new MenuItemResponseDto(
                UUID.randomUUID().toString(),
                "Prosciutto Crudo",
                "Lorem ipsum",
                BigDecimal.valueOf(32.50),
                "crudo.jpg",
                true
        );
    }

    private MenuItemRequestDto sampleInvalidRequestDto() {
        return new MenuItemRequestDto(
                "",
                "",
                BigDecimal.valueOf(32.50),
                true,
                "crudo.jpg",
                UUID.randomUUID(),
                UUID.randomUUID()
        );
    }
}
