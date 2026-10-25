package com.qa.tests;

import com.qa.mobile.models.Product;
import com.qa.mobile.screens.ProductScreen;
import com.qa.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ProductTest extends BaseTest {

    @DataProvider(name = "products")
    public Object[][] products() {
        // First tile, a mid-list tile and the last tile (needs scrolling on most screens).
        return new Object[][] {{Product.BACKPACK}, {Product.BOLT_T_SHIRT}, {Product.RED_T_SHIRT}};
    }

    @Test(dataProvider = "products", groups = "regression", description = "Product page shows the right name and price")
    public void productPageShowsNameAndPrice(Product product) {
        ProductScreen page = catalog.openProduct(product);

        Assert.assertTrue(page.name().contains(product.displayName()), "Product name: " + page.name());
        Assert.assertEquals(page.price(), product.price());
        Assert.assertEquals(page.quantity(), 1, "Quantity should start at 1");
    }
}
