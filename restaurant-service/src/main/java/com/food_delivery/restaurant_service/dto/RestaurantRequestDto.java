package com.food_delivery.restaurant_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class RestaurantRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Cuisine type is required")
    private String cuisineType;

    @NotBlank(message = "Street is required")
    private String street;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Postal code is required")
    private String postalCode;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    private boolean active;

    @NotNull(message = "Owner id is required")
    private UUID ownerId;

    public RestaurantRequestDto(String name, String cuisineType, String street, String city, String postalCode,
                                String phoneNumber, boolean active, UUID ownerId) {
        this.name = name;
        this.cuisineType = cuisineType;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.active = active;
        this.ownerId = ownerId;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }
}
