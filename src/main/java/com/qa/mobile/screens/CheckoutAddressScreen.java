package com.qa.mobile.screens;

import com.qa.mobile.models.ShippingAddress;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class CheckoutAddressScreen extends BaseScreen {

    private final By fullName = id("Full Name* input field");
    private final By address1 = id("Address Line 1* input field");
    private final By address2 = id("Address Line 2 input field");
    private final By city = id("City* input field");
    private final By state = id("State/Region input field");
    private final By zip = id("Zip Code* input field");
    private final By country = id("Country* input field");
    private final By toPayment = id("To Payment button");

    public CheckoutAddressScreen(AppiumDriver driver) {
        super(driver);
    }

    @Override
    protected By screenMarker() {
        return id("checkout address screen");
    }

    public CheckoutAddressScreen waitUntilLoaded() {
        waitUntilShown();
        return this;
    }

    public CheckoutPaymentScreen enterAddress(ShippingAddress a) {
        type(fullName, screenMarker(), a.fullName());
        type(address1, screenMarker(), a.addressLine1());
        type(address2, screenMarker(), a.addressLine2());
        type(city, screenMarker(), a.city());
        type(state, screenMarker(), a.stateRegion());
        type(zip, screenMarker(), a.zipCode());
        type(country, screenMarker(), a.country());
        tap(toPayment, screenMarker());
        return new CheckoutPaymentScreen(driver).waitUntilLoaded();
    }
}
