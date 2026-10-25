package com.qa.mobile.driver;

public enum Platform {
    ANDROID, IOS;

    public static Platform from(String value) {
        return switch (value.trim().toLowerCase()) {
            case "android" -> ANDROID;
            case "ios" -> IOS;
            default -> throw new IllegalArgumentException("platform must be android or ios, not '" + value + "'");
        };
    }
}
