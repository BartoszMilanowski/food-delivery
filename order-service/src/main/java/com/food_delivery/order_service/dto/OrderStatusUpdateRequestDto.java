package com.food_delivery.order_service.dto;

import com.food_delivery.order_service.model.OrderStatus;
import jakarta.validation.constraints.NotNull;

public class OrderStatusUpdateRequestDto {

    @NotNull(message = "Status is required")
    private OrderStatus status;

    public OrderStatusUpdateRequestDto(){}

    public OrderStatus getStatus(){
        return status;
    }

    public void setStatus(OrderStatus status){
        this.status = status;
    }
}
