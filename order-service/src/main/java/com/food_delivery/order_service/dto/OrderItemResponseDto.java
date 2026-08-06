package com.food_delivery.order_service.dto;

import java.math.BigDecimal;

public class OrderItemResponseDto {

    private String id;
    private String menuItemId;
    private String menuItemName;
    private BigDecimal unitPrice;
    private int quantity;

    public OrderItemResponseDto() {
    }

    public OrderItemResponseDto(String id, String menuItemId, String menuItemName, BigDecimal unitPrice, int quantity) {
        this.id = id;
        this.menuItemId = menuItemId;
        this.menuItemName = menuItemName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(String menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getMenuItemName() {
        return menuItemName;
    }

    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}