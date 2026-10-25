package com.qa.mobile.screens;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class CheckoutCompleteScreen extends BaseScreen {

    private final By continueShopping = id("Continue Shopping button");

    public CheckoutCompleteScreen(AppiumDriver driver) {
        super(driver);
    }

    @Override
    protected By screenMarker() {
        return id("checkout complete screen");
    }

    public CheckoutCompleteScreen waitUntilLoaded() {
        waitUntilShown();
        return this;
    }

    public boolean showsConfirmation() {
        return isShown(textLocator("Checkout Complete"));
    }

    public CatalogScreen continueShopping() {
        tap(continueShopping, screenMarker());
        return new CatalogScreen(driver).waitUntilLoaded();
    }
}
