package com.qa.tests;

import com.qa.mobile.models.Product;
import com.qa.mobile.screens.CartScreen;
import com.qa.mobile.screens.NavBar;
import com.qa.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;

public class CartTest extends BaseTest {

    @Test(groups = {"smoke", "regression"}, description = "Cart badge, item count and total update for several products")
    public void cartTotalsAcrossProducts() {
        NavBar nav = new NavBar(driver());
        catalog.openProduct(Product.BACKPACK).addToCart(2).backToCatalog();
        Assert.assertEquals(nav.cartCount(), 2, "cart badge after 2 backpacks");

        catalog.openProduct(Product.BIKE_LIGHT).addToCart(1);
        Assert.assertEquals(nav.cartCount(), 3, "cart badge after adding a bike light");

        CartScreen cart = nav.openCart();
        Assert.assertEquals(cart.rowCount(), 2, "distinct products in cart");
        Assert.assertEquals(cart.totalItems(), 3);
        BigDecimal expected = Product.BACKPACK.price().multiply(BigDecimal.valueOf(2)).add(Product.BIKE_LIGHT.price());
        Assert.assertEquals(cart.totalPrice(), expected);
    }

    @Test(groups = "regression", description = "Removing the only product empties the cart")
    public void removingLastItemEmptiesCart() {
        CartScreen cart = catalog.openProduct(Product.ONESIE).addToCart(1).nav().openCart();
        Assert.assertTrue(cart.contains(Product.ONESIE.displayName()));

        cart.remove(Product.ONESIE.displayName());

        Assert.assertTrue(cart.isEmpty(), "Expected the empty-cart view");
        Assert.assertEquals(new NavBar(driver()).cartCount(), 0);
    }
}
