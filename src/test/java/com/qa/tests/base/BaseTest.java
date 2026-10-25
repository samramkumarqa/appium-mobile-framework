package com.qa.tests.base;

import com.qa.mobile.driver.DriverFactory;
import com.qa.mobile.reporting.ExtentManager;
import com.qa.mobile.screens.CatalogScreen;
import io.appium.java_client.AppiumDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Each test gets a fresh Appium session with a clean app (empty cart, logged out),
 * so tests are independent and can run in any order.
 * Reporting is wired in automatically through TestListener (registered in META-INF/services).
 */
public abstract class BaseTest {

    protected CatalogScreen catalog;

    @BeforeMethod(alwaysRun = true)
    public void launchApp() {
        DriverFactory.start();
        catalog = new CatalogScreen(driver()).waitUntilLoaded();
    }

    @AfterMethod(alwaysRun = true)
    public void closeApp() {
        DriverFactory.quit();
    }

    protected AppiumDriver driver() {
        return DriverFactory.getDriver();
    }

    protected void step(String description) {
        if (ExtentManager.current() != null) {
            ExtentManager.current().info(description);
        }
    }
}
