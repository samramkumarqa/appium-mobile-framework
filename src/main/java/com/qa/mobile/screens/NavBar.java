package com.qa.mobile.screens;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Cart and menu entry points. Android shows them in a header, iOS in a bottom tab bar,
 * so this is the one place with different locators per platform.
 */
public class NavBar extends BaseScreen {

    public NavBar(AppiumDriver driver) {
        super(driver);
    }

    @Override
    protected By screenMarker() {
        return cartButton();
    }

    private By cartButton() {
        return id(isAndroid() ? "cart badge" : "tab bar option cart");
    }

    private By menuButton() {
        return id(isAndroid() ? "open menu" : "tab bar option menu");
    }

    public CartScreen openCart() {
        tap(cartButton());
        return new CartScreen(driver).waitUntilLoaded();
    }

    /** Number shown on the cart icon; 0 when it shows nothing. */
    public int cartCount() {
        List<WebElement> found = driver.findElements(cartButton());
        if (found.isEmpty()) {
            return 0;
        }
        String value = isAndroid() ? textOf(found.get(0)) : found.get(0).getAttribute("label");
        String digits = value == null ? "" : value.replaceAll("[^0-9]", "");
        return digits.isEmpty() ? 0 : Integer.parseInt(digits);
    }

    public LoginScreen openLoginFromMenu() {
        tap(menuButton());
        tap(id("menu item log in"), id("menu item catalog"));
        return new LoginScreen(driver).waitUntilLoaded();
    }
}
