package com.food_delivery.order_service.client;

import com.food_delivery.order_service.client.dto.MenuItemSummaryDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import java.util.UUID;

public interface MenuItemClient {

    @GetExchange("/menu/items/{id}")
    MenuItemSummaryDto getMenuItemById(@PathVariable UUID id);
}
