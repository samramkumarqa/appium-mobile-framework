package com.qa.mobile.screens;

import com.qa.mobile.config.Config;
import com.qa.mobile.driver.DriverFactory;
import com.qa.mobile.driver.Platform;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.HasOnScreenKeyboard;
import io.appium.java_client.HidesKeyboard;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Shared actions for every screen, written once for both platforms.
 *
 * The app sets the same accessibility id on Android (content-desc) and iOS (name),
 * so {@link #id(String)} works everywhere. Only a few gestures and text reads differ by platform.
 */
public abstract class BaseScreen {

    private static final int MAX_SWIPES = 6;

    protected final AppiumDriver driver;
    protected final Platform platform;
    protected final WebDriverWait wait;

    protected BaseScreen(AppiumDriver driver) {
        this.driver = driver;
        this.platform = DriverFactory.platform();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Config.getInt("timeout.seconds")));
    }

    /** The accessibility id that marks this screen as loaded. */
    protected abstract By screenMarker();

    public boolean isShown() {
        return isShown(screenMarker());
    }

    protected void waitUntilShown() {
        visible(screenMarker());
    }

    protected static By id(String accessibilityId) {
        return AppiumBy.accessibilityId(accessibilityId);
    }

    protected boolean isAndroid() {
        return platform == Platform.ANDROID;
    }

    protected WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected boolean isShown(By locator) {
        List<WebElement> found = driver.findElements(locator);
        return !found.isEmpty() && found.get(0).isDisplayed();
    }

    protected void tap(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    /** Scrolls the given container until the target is on screen, then taps it. */
    protected void tap(By target, By scrollContainer) {
        scrollTo(target, scrollContainer).click();
    }

    protected void type(By field, By scrollContainer, String text) {
        WebElement element = scrollTo(field, scrollContainer);
        element.click();
        element.clear();
        element.sendKeys(text);
        hideKeyboard();
    }

    protected String textOf(By locator) {
        return textOf(visible(locator));
    }

    /**
     * Visible text of an element. On Android a container's text lives in its child TextViews,
     * so those are joined; on iOS the element's label already holds it.
     */
    protected String textOf(WebElement element) {
        if (isAndroid()) {
            List<WebElement> texts = element.findElements(By.xpath(".//android.widget.TextView"));
            if (!texts.isEmpty()) {
                return texts.stream().map(WebElement::getText).collect(Collectors.joining(" ")).trim();
            }
        }
        String text = element.getText();
        return text == null ? "" : text.trim();
    }

    /** Text of an optional element, or "" when it is not on screen (e.g. no error shown). */
    protected String textIfShown(By locator) {
        List<WebElement> found = driver.findElements(locator);
        return found.isEmpty() ? "" : textOf(found.get(0));
    }

    /**
     * Swipes inside the container until the target is displayed.
     * Uses Appium's own gesture commands instead of fixed screen coordinates, so it works on any screen size.
     */
    protected WebElement scrollTo(By target, By container) {
        for (int attempt = 0; attempt <= MAX_SWIPES; attempt++) {
            for (WebElement element : driver.findElements(target)) {
                if (element.isDisplayed()) {
                    return element;
                }
            }
            if (attempt < MAX_SWIPES) {
                swipeUpInside(visible(container));
            }
        }
        throw new NoSuchElementException("Could not scroll to " + target + " inside " + container);
    }

    private void swipeUpInside(WebElement container) {
        String elementId = ((RemoteWebElement) container).getId();
        if (isAndroid()) {
            driver.executeScript("mobile: scrollGesture",
                    Map.of("elementId", elementId, "direction", "down", "percent", 0.6));
        } else {
            driver.executeScript("mobile: swipe", Map.of("elementId", elementId, "direction", "up"));
        }
    }

    protected void hideKeyboard() {
        try {
            if (driver instanceof HasOnScreenKeyboard keyboard && !keyboard.isKeyboardShown()) {
                return;
            }
            if (isAndroid()) {
                ((HidesKeyboard) driver).hideKeyboard();
            } else {
                // iOS has no generic "hide": press whichever dismiss key the current keyboard offers.
                driver.executeScript("mobile: hideKeyboard", Map.of("keys", List.of("Return", "Done", "return", "done")));
            }
        } catch (WebDriverException ignored) {
            // Keyboard already gone or has no dismiss key; the next tap still works.
        }
    }

    /** Waits until an element with this text is on screen (used for headings without their own id). */
    protected By textLocator(String exactText) {
        return isAndroid()
                ? AppiumBy.androidUIAutomator("new UiSelector().text(\"" + exactText + "\")")
                : AppiumBy.iOSNsPredicateString("label == \"" + exactText + "\"");
    }
}
