package com.qa.mobile.screens;

import com.qa.mobile.utils.Money;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

import java.math.BigDecimal;

public class ProductScreen extends BaseScreen {

    private final By name = id("container header");
    private final By price = id("product price");
    private final By plus = id("counter plus button");
    private final By amount = id("counter amount");
    private final By addToCart = id("Add To Cart button");

    public ProductScreen(AppiumDriver driver) {
        super(driver);
    }

    @Override
    protected By screenMarker() {
        return id("product screen");
    }

    public ProductScreen waitUntilLoaded() {
        waitUntilShown();
        visible(price);
        return this;
    }

    public String name() {
        return textOf(name);
    }

    public BigDecimal price() {
        return Money.parse(textOf(price));
    }

    public int quantity() {
        return Money.parseCount(textOf(scrollTo(amount, screenMarker())));
    }

    /** Sets the quantity with the + button (it starts at 1), then adds to cart. */
    public ProductScreen addToCart(int qty) {
        if (qty < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }
        for (int i = 1; i < qty; i++) {
            tap(plus, screenMarker());
        }
        wait.until(d -> quantity() == qty);
        tap(addToCart, screenMarker());
        return this;
    }

    /** Android uses the system back button; iOS uses the app's own back arrow. */
    public CatalogScreen backToCatalog() {
        if (isAndroid()) {
            driver.navigate().back();
        } else {
            tap(id("navigation back button"));
        }
        return new CatalogScreen(driver).waitUntilLoaded();
    }

    public NavBar nav() {
        return new NavBar(driver);
    }
}
