package com.qa.mobile.models;

public record ShippingAddress(String fullName, String addressLine1, String addressLine2,
                              String city, String stateRegion, String zipCode, String country) {

    public static ShippingAddress sample() {
        return new ShippingAddress("Ram Kumar", "12 Anna Salai", "Flat 4B", "Chennai", "Tamil Nadu", "600002", "India");
    }
}
