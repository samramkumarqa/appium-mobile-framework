package com.qa.mobile.screens;

import com.qa.mobile.utils.Money;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

import java.math.BigDecimal;

public class ReviewOrderScreen extends BaseScreen {

    /** Flat delivery fee the app adds on the review screen. */
    public static final BigDecimal DELIVERY_FEE = new BigDecimal("5.99");

    private final By deliveryAddress = id("checkout delivery address");
    private final By paymentInfo = id("checkout payment info");
    private final By totalItems = id("total number");
    private final By totalPrice = id("total price");
    private final By placeOrder = id("Place Order button");

    public ReviewOrderScreen(AppiumDriver driver) {
        super(driver);
    }

    @Override
    protected By screenMarker() {
        return id("checkout review order screen");
    }

    public ReviewOrderScreen waitUntilLoaded() {
        waitUntilShown();
        return this;
    }

    public String deliveryAddress() {
        return textOf(scrollTo(deliveryAddress, screenMarker()));
    }

    public String paymentInfo() {
        return textOf(scrollTo(paymentInfo, screenMarker()));
    }

    public int totalItems() {
        return Money.parseCount(textOf(totalItems));
    }

    public BigDecimal totalPrice() {
        return Money.parse(textOf(totalPrice));
    }

    public CheckoutCompleteScreen placeOrder() {
        tap(placeOrder);
        return new CheckoutCompleteScreen(driver).waitUntilLoaded();
    }
}
