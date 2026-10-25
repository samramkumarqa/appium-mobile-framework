package com.qa.mobile.driver;

import com.qa.mobile.config.Config;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

/**
 * Starts one Appium session per test thread. Every session starts the app from a clean state
 * (noReset=false), so tests never depend on each other.
 */
public final class DriverFactory {

    private static final ThreadLocal<AppiumDriver> DRIVER = new ThreadLocal<>();
    private static final ThreadLocal<Platform> PLATFORM = new ThreadLocal<>();

    private DriverFactory() {
    }

    public static AppiumDriver getDriver() {
        AppiumDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("No Appium session for this thread. Call DriverFactory.start() first.");
        }
        return driver;
    }

    public static Platform platform() {
        Platform platform = PLATFORM.get();
        return platform != null ? platform : Platform.from(Config.get("platform"));
    }

    public static boolean hasDriver() {
        return DRIVER.get() != null;
    }

    public static AppiumDriver start() {
        Platform platform = Platform.from(Config.get("platform"));
        URL server = serverUrl();
        Duration commandTimeout = Duration.ofSeconds(Config.getInt("new.command.timeout.seconds"));

        AppiumDriver driver = switch (platform) {
            case ANDROID -> {
                UiAutomator2Options options = new UiAutomator2Options()
                        .setDeviceName(Config.get("android.device.name"))
                        .setApp(resolveApp(Config.get("android.app")))
                        .setAutoGrantPermissions(true)
                        .setNoReset(false)
                        .setNewCommandTimeout(commandTimeout);
                String udid = Config.getOrNull("android.udid");
                if (udid != null) {
                    options.setUdid(udid);
                }
                String avd = Config.getOrNull("android.avd");
                if (avd != null) {
                    options.setAvd(avd);
                }
                yield new AndroidDriver(server, options);
            }
            case IOS -> {
                XCUITestOptions options = new XCUITestOptions()
                        .setDeviceName(Config.get("ios.device.name"))
                        .setApp(resolveApp(Config.get("ios.app")))
                        .setNoReset(false)
                        .setNewCommandTimeout(commandTimeout)
                        .setWdaLaunchTimeout(Duration.ofMinutes(3));
                String version = Config.getOrNull("ios.platform.version");
                if (version != null) {
                    options.setPlatformVersion(version);
                }
                yield new IOSDriver(server, options);
            }
        };

        DRIVER.set(driver);
        PLATFORM.set(platform);
        return driver;
    }

    public static void quit() {
        AppiumDriver driver = DRIVER.get();
        try {
            if (driver != null) {
                driver.quit();
            }
        } finally {
            DRIVER.remove();
            PLATFORM.remove();
        }
    }

    private static URL serverUrl() {
        String url = Config.get("appium.url");
        try {
            return URI.create(url).toURL();
        } catch (MalformedURLException | IllegalArgumentException e) {
            throw new IllegalStateException("Invalid appium.url: " + url, e);
        }
    }

    /** Appium needs an absolute path (or a URL). Fails early with a clear message if the build is missing. */
    private static String resolveApp(String app) {
        if (app.startsWith("http://") || app.startsWith("https://")) {
            return app;
        }
        Path path = Path.of(app).toAbsolutePath().normalize();
        if (!Files.exists(path)) {
            throw new IllegalStateException("App build not found at " + path
                    + ". Download it from https://github.com/saucelabs/my-demo-app-rn/releases (see apps/README.md).");
        }
        return path.toString();
    }
}
