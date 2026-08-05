package com.food_delivery.order_service.client;

import com.food_delivery.order_service.client.dto.RestaurantSummaryDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import java.util.UUID;

public interface RestaurantClient {

    @GetExchange("/restaurants/{id}")
    RestaurantSummaryDto getRestaurantById(@PathVariable UUID id);
}
