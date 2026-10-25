package com.qa.mobile.screens;

import com.qa.mobile.utils.Money;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.math.BigDecimal;

public class CartScreen extends BaseScreen {

    private final By rows = id("product row");
    private final By totalItems = id("total number");
    private final By totalPrice = id("total price");
    private final By proceedToCheckout = id("Proceed To Checkout button");
    private final By goShopping = id("Go Shopping button");

    public CartScreen(AppiumDriver driver) {
        super(driver);
    }

    @Override
    protected By screenMarker() {
        return id("cart screen");
    }

    public CartScreen waitUntilLoaded() {
        waitUntilShown();
        return this;
    }

    public int rowCount() {
        return driver.findElements(rows).size();
    }

    public boolean contains(String productName) {
        return !driver.findElements(row(productName)).isEmpty();
    }

    /** "3 items" in the footer, as a number. */
    public int totalItems() {
        return Money.parseCount(textOf(totalItems));
    }

    public BigDecimal totalPrice() {
        return Money.parse(textOf(totalPrice));
    }

    public CartScreen remove(String productName) {
        scrollTo(row(productName), screenMarker()).findElement(id("remove item")).click();
        wait.until(ExpectedConditions.numberOfElementsToBe(row(productName), 0));
        return this;
    }

    public boolean isEmpty() {
        return isShown(goShopping);
    }

    /** Logged-out users are sent to the login screen first. */
    public LoginScreen proceedToCheckoutAsGuest() {
        tap(proceedToCheckout);
        return new LoginScreen(driver).waitUntilLoaded();
    }

    public CheckoutAddressScreen proceedToCheckout() {
        tap(proceedToCheckout);
        return new CheckoutAddressScreen(driver).waitUntilLoaded();
    }

    private By row(String name) {
        return isAndroid()
                ? AppiumBy.xpath("//android.widget.TextView[@text=\"" + name + "\"]/ancestor::*[@content-desc=\"product row\"]")
                : AppiumBy.iOSClassChain("**/XCUIElementTypeOther[`name == \"product row\" AND label CONTAINS \"" + name + "\"`]");
    }
}
