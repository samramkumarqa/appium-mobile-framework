package com.qa.mobile.models;

/**
 * One row of purchase test data (src/test/resources/testdata/purchases.json).
 *
 * @param product  a display name ("Sauce Labs Backpack") or enum name ("BACKPACK")
 * @param quantity how many to add
 * @param run      false keeps the row but skips it
 */
public record PurchaseScenario(String id, String product, int quantity, boolean run) {

    public Product toProduct() {
        return Product.fromName(product);
    }

    @Override
    public String toString() {
        return id + " (" + quantity + " x " + product + ")";
    }
}
