package com.food_delivery.restaurant_service.dto;

public class RestaurantResponseDto {

    private String id;
    private String name;
    private String cuisineType;
    private String street;
    private String city;
    private String postalCode;
    private String phoneNumber;

    public RestaurantResponseDto() {
    }

    public RestaurantResponseDto(String id, String name, String cuisineType,
                                 String street, String city, String postalCode, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.cuisineType = cuisineType;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
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

    public String getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
