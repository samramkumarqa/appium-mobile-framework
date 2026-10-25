package com.qa.mobile.screens;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class LoginScreen extends BaseScreen {

    /** Demo accounts printed on the app's own login screen. */
    public static final String STANDARD_USER = "bob@example.com";
    public static final String LOCKED_OUT_USER = "alice@example.com";
    public static final String PASSWORD = "10203040";

    private final By username = id("Username input field");
    private final By password = id("Password input field");
    private final By loginButton = id("Login button");
    private final By usernameError = id("Username-error-message");
    private final By passwordError = id("Password-error-message");
    private final By genericError = id("generic-error-message");

    public LoginScreen(AppiumDriver driver) {
        super(driver);
    }

    @Override
    protected By screenMarker() {
        return id("login screen");
    }

    public LoginScreen waitUntilLoaded() {
        waitUntilShown();
        visible(username);
        return this;
    }

    /** After "Proceed To Checkout", a successful login continues to the address step. */
    public CheckoutAddressScreen loginForCheckout(String user, String pass) {
        submit(user, pass);
        return new CheckoutAddressScreen(driver).waitUntilLoaded();
    }

    /** From the menu, a successful login returns to the catalog. */
    public CatalogScreen loginFromMenu(String user, String pass) {
        submit(user, pass);
        return new CatalogScreen(driver).waitUntilLoaded();
    }

    public LoginScreen loginExpectingError(String user, String pass) {
        submit(user, pass);
        return this;
    }

    public String usernameError() {
        return textIfShown(usernameError);
    }

    public String passwordError() {
        return textIfShown(passwordError);
    }

    public String genericError() {
        return textIfShown(genericError);
    }

    private void submit(String user, String pass) {
        if (!user.isEmpty()) {
            type(username, screenMarker(), user);
        }
        if (!pass.isEmpty()) {
            type(password, screenMarker(), pass);
        }
        tap(loginButton, screenMarker());
    }
}
