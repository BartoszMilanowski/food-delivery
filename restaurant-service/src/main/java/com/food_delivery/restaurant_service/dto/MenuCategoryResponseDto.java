package com.food_delivery.restaurant_service.dto;

public class MenuCategoryResponseDto {

    private String id;
    private String name;
    private int displayOrder;

    public MenuCategoryResponseDto() {
    }

    public MenuCategoryResponseDto(String id, String name, int displayOrder) {
        this.id = id;
        this.name = name;
        this.displayOrder = displayOrder;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDisplayOrder(){
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder){
        this.displayOrder = displayOrder;
    }


}
