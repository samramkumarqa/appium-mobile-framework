package com.qa.mobile.utils;

import java.math.BigDecimal;

/** Parses the app's labels: "$59.98" -> 59.98, "2 items" -> 2. */
public final class Money {

    private Money() {
    }

    public static BigDecimal parse(String label) {
        String number = label.replaceAll("[^0-9.]", "");
        if (number.isEmpty()) {
            throw new IllegalArgumentException("No amount found in '" + label + "'");
        }
        return new BigDecimal(number);
    }

    public static int parseCount(String label) {
        String digits = label.replaceAll("[^0-9]", "");
        return digits.isEmpty() ? 0 : Integer.parseInt(digits);
    }
}
