package com.food_delivery.restaurant_service.controller;


import tools.jackson.databind.ObjectMapper;
import com.food_delivery.restaurant_service.dto.RestaurantRequestDto;
import com.food_delivery.restaurant_service.dto.RestaurantResponseDto;
import com.food_delivery.restaurant_service.exception.RestaurantNotFoundException;
import com.food_delivery.restaurant_service.service.RestaurantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.eq;

@WebMvcTest(RestaurantController.class)
public class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RestaurantService restaurantService;

    @Test
    void getRestaurants_shouldReturnList() throws Exception {
        RestaurantResponseDto dto = sampleResponseDto();
        when(restaurantService.getRestaurants()).thenReturn(List.of(dto));

        mockMvc.perform(get("/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Pizza Napoli"));
    }

    @Test
    void getRestaurantById_whenExists_shouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();
        RestaurantResponseDto dto = sampleResponseDto();
        when(restaurantService.getRestaurantById(id)).thenReturn(dto);

        mockMvc.perform(get("/restaurants/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pizza Napoli"));
    }

    @Test
    void getRestaurantById_whenNotFound_shouldReturn404() throws Exception {
        UUID id = UUID.randomUUID();
        when(restaurantService.getRestaurantById(id)).thenThrow(new RestaurantNotFoundException(id));

        mockMvc.perform(get("/restaurants/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void createRestaurant_withValidBody_shouldReturn201() throws Exception {
        RestaurantRequestDto requestDto = sampleRequestDto();
        RestaurantResponseDto responseDto = sampleResponseDto();
        when(restaurantService.createRestaurant(any(RestaurantRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Pizza Napoli"));
    }

    @Test
    void createRestaurant_withBlankName_shouldReturn400() throws Exception {
        RestaurantRequestDto invalid = invalidSampleRequestDto();

        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.name").value("Name is required"));

        verify(restaurantService, never()).createRestaurant(any());
    }

    @Test
    void updateRestaurant_withValidBody_shouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();
        RestaurantRequestDto requestDto = sampleRequestDto();
        RestaurantResponseDto responseDto = sampleResponseDto();
        when(restaurantService.updateRestaurant(eq(id), any(RestaurantRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(put("/restaurants/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pizza Napoli"));
    }

    @Test
    void updateRestaurant_withBlankName_shouldReturn400() throws Exception {
        UUID id = UUID.randomUUID();
        RestaurantRequestDto invalid = invalidSampleRequestDto();

        mockMvc.perform(put("/restaurants/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.name").value("Name is required"));

        verify(restaurantService, never()).updateRestaurant(any(), any());
    }

    @Test
    void deleteRestaurant_whenExists_shouldReturn204() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(restaurantService).deleteRestaurant(id);

        mockMvc.perform(delete("/restaurants/{id}", id))
                .andExpect(status().isNoContent());

        verify(restaurantService).deleteRestaurant(id);
    }

    @Test
    void searchRestaurant_shouldPassParamsToService() throws Exception {
        when(restaurantService.searchRestaurants("Warszawa", null, true, null, null))
                .thenReturn(List.of(sampleResponseDto()));

        mockMvc.perform(get("/restaurants/search")
                        .param("city", "Warszawa")
                        .param("active", "true"))
                .andExpect(status().isOk());

        verify(restaurantService).searchRestaurants("Warszawa", null, true, null, null);

    }

    @Test
    void searchRestaurantByOwnerId_shouldPassParamsToService() throws Exception {
        UUID ownerId = UUID.randomUUID();
        when(restaurantService.searchRestaurants(null, null, null, null, ownerId))
                .thenReturn(List.of(sampleResponseDto()));

        mockMvc.perform(get("/restaurants/owner/{ownerId}", ownerId)
                        .param("ownerId", ownerId.toString()))
                .andExpect(status().isOk());

        verify(restaurantService).searchRestaurants(null, null, null, null, ownerId);
    }

    private RestaurantRequestDto sampleRequestDto() {
        return new RestaurantRequestDto(
                "Pizza Napoli", "Italian", "Kwiatowa 1", "Warszawa",
                "00-001", "123456789", true, UUID.randomUUID()
        );
    }

    private RestaurantRequestDto invalidSampleRequestDto() {
        return new RestaurantRequestDto(
                "", "Italian", "Kwiatowa 1", "Warszawa", "00-001",
                "123456789", true, UUID.randomUUID()
        );
    }

    private RestaurantResponseDto sampleResponseDto() {
        RestaurantResponseDto dto = new RestaurantResponseDto();
        dto.setId(UUID.randomUUID().toString());
        dto.setName("Pizza Napoli");
        dto.setCuisineType("Italian");
        return dto;
    }
}
