package com.food_delivery.order_service.client;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.service.registry.ImportHttpServices;

@Configuration(proxyBeanMethods = false)
@ImportHttpServices(group = "restaurant", types = {RestaurantClient.class, MenuItemClient.class})
public class HttpClientsConfig {
}
