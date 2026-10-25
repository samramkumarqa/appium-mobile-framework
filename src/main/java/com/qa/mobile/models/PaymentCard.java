package com.qa.mobile.models;

/** Test card values. The demo app only validates the format; nothing is charged. */
public record PaymentCard(String fullName, String number, String expiry, String securityCode) {

    /** expiry is typed as MMYY; the app formats it as MM/YY. */
    public static PaymentCard sample() {
        return new PaymentCard("Ram Kumar", "5555555555554444", "0330", "123");
    }
}
