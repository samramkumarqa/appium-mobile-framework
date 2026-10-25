package com.qa.mobile.screens;

import com.qa.mobile.models.PaymentCard;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class CheckoutPaymentScreen extends BaseScreen {

    // With "billing address same as shipping" ticked (the default) there is a single Full Name field.
    private final By fullName = id("Full Name* input field");
    private final By cardNumber = id("Card Number* input field");
    private final By expiry = id("Expiration Date* input field");
    private final By securityCode = id("Security Code* input field");
    private final By reviewOrder = id("Review Order button");

    public CheckoutPaymentScreen(AppiumDriver driver) {
        super(driver);
    }

    @Override
    protected By screenMarker() {
        return id("checkout payment screen");
    }

    public CheckoutPaymentScreen waitUntilLoaded() {
        waitUntilShown();
        return this;
    }

    public ReviewOrderScreen enterCard(PaymentCard card) {
        type(fullName, screenMarker(), card.fullName());
        type(cardNumber, screenMarker(), card.number());
        type(expiry, screenMarker(), card.expiry());
        type(securityCode, screenMarker(), card.securityCode());
        tap(reviewOrder, screenMarker());
        return new ReviewOrderScreen(driver).waitUntilLoaded();
    }
}
