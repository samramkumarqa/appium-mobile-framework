package com.qa.mobile.models;

import java.math.BigDecimal;

/** The demo app's catalog, with the prices it ships with. */
public enum Product {
    BACKPACK("Sauce Labs Backpack", "29.99"),
    BIKE_LIGHT("Sauce Labs Bike Light", "9.99"),
    BOLT_T_SHIRT("Sauce Labs Bolt T-Shirt", "15.99"),
    FLEECE_JACKET("Sauce Labs Fleece Jacket", "49.99"),
    ONESIE("Sauce Labs Onesie", "7.99"),
    RED_T_SHIRT("Test.allTheThings() T-Shirt", "15.99");

    private final String displayName;
    private final BigDecimal price;

    Product(String displayName, String price) {
        this.displayName = displayName;
        this.price = new BigDecimal(price);
    }

    public String displayName() {
        return displayName;
    }

    public BigDecimal price() {
        return price;
    }

    public static Product fromName(String name) {
        for (Product p : values()) {
            if (p.displayName.equalsIgnoreCase(name.trim()) || p.name().equalsIgnoreCase(name.trim())) {
                return p;
            }
        }
        throw new IllegalArgumentException("Unknown product: " + name);
    }

    @Override
    public String toString() {
        return displayName;
    }
}
