package com.food_delivery.restaurant_service.controller;

import com.food_delivery.restaurant_service.dto.MenuCategoryRequestDto;
import com.food_delivery.restaurant_service.dto.MenuCategoryResponseDto;
import com.food_delivery.restaurant_service.exception.MenuCategoryNotFoundException;
import com.food_delivery.restaurant_service.service.MenuCategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@WebMvcTest(MenuCategoryController.class)
public class MenuCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MenuCategoryService menuCategoryService;


    @Test
    void getMenuCategories_shouldReturnList() throws Exception {
        MenuCategoryResponseDto dto = sampleResponseDto();
        when(menuCategoryService.getMenuCategories()).thenReturn(List.of(dto));

        mockMvc.perform(get("/menu/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Pizza"));
    }

    @Test
    void getMenuCategoryById_whenExists_shouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();
        MenuCategoryResponseDto dto = sampleResponseDto();
        when(menuCategoryService.getMenuCategoryById(id)).thenReturn(dto);

        mockMvc.perform(get("/menu/categories/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pizza"));
    }

    @Test
    void getMenuCategoryById_when_NotFound_shouldReturn404() throws Exception {
        UUID id = UUID.randomUUID();
        MenuCategoryResponseDto dto = sampleResponseDto();
        when(menuCategoryService.getMenuCategoryById(id)).thenThrow(new MenuCategoryNotFoundException(id));

        mockMvc.perform(get("/menu/categories/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void createMenuCategory_withValidBody_shouldReturn200() throws Exception {
        MenuCategoryRequestDto requestDto = sampleRequestDto();
        MenuCategoryResponseDto responseDto = sampleResponseDto();
        when(menuCategoryService.createMenuCategory(any(MenuCategoryRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/menu/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Pizza"));
    }

    @Test
    void createMenuCategory_withBlankName_shouldReturn400() throws Exception {
        MenuCategoryRequestDto invalidDto = invalidSampleRequestDto();

        mockMvc.perform(post("/menu/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.name").value("Name is required"));

        verify(menuCategoryService, never()).createMenuCategory(any());
    }

    @Test
    void updateMenuCategory_withValidBody_shouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();
        MenuCategoryRequestDto requestDto = sampleRequestDto();
        MenuCategoryResponseDto responseDto = sampleResponseDto();
        when(menuCategoryService.updateMenuCategory(eq(id), any(MenuCategoryRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(put("/menu/categories/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pizza"));
    }

    @Test
    void updateMenuCategory_withBlankName_shouldReturn400() throws Exception {
        UUID id = UUID.randomUUID();
        MenuCategoryRequestDto invalidDto = invalidSampleRequestDto();

        mockMvc.perform(put("/menu/categories/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.name").value("Name is required"));

        verify(menuCategoryService, never()).updateMenuCategory(any(), any());
    }

    @Test
    void deleteMenuCategory_whenExist_shouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(menuCategoryService).deleteMenuCategory(id);

        mockMvc.perform(delete("/menu/categories/{id}", id))
                .andExpect(status().isNoContent());

        verify(menuCategoryService).deleteMenuCategory(id);
    }

    private MenuCategoryRequestDto sampleRequestDto() {
        return new MenuCategoryRequestDto(
                "Pizza", 3, UUID.randomUUID()
        );
    }

    private MenuCategoryRequestDto invalidSampleRequestDto() {
        return new MenuCategoryRequestDto(
                "", 3, UUID.randomUUID()
        );
    }

    private MenuCategoryResponseDto sampleResponseDto() {
        MenuCategoryResponseDto menuCategoryResponseDto = new MenuCategoryResponseDto();
        menuCategoryResponseDto.setId(UUID.randomUUID().toString());
        menuCategoryResponseDto.setName("Pizza");
        menuCategoryResponseDto.setDisplayOrder(3);

        return menuCategoryResponseDto;
    }
}
