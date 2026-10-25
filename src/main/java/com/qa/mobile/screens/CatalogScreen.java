package com.qa.mobile.screens;

import com.qa.mobile.models.Product;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class CatalogScreen extends BaseScreen {

    public CatalogScreen(AppiumDriver driver) {
        super(driver);
    }

    @Override
    protected By screenMarker() {
        return id("products screen");
    }

    public CatalogScreen waitUntilLoaded() {
        waitUntilShown();
        visible(id("store item"));
        return this;
    }

    public NavBar nav() {
        return new NavBar(driver);
    }

    public ProductScreen openProduct(Product product) {
        tap(storeItem(product.displayName()), screenMarker());
        return new ProductScreen(driver).waitUntilLoaded();
    }

    /** A product tile, found by the product name it contains. */
    private By storeItem(String name) {
        return isAndroid()
                ? AppiumBy.xpath("//android.widget.TextView[@text=\"" + name + "\"]/ancestor::*[@content-desc=\"store item\"]")
                : AppiumBy.iOSClassChain("**/XCUIElementTypeOther[`name == \"store item\" AND label CONTAINS \"" + name + "\"`]");
    }
}
