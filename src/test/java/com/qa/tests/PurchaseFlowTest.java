package com.qa.tests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.mobile.models.PaymentCard;
import com.qa.mobile.models.Product;
import com.qa.mobile.models.PurchaseScenario;
import com.qa.mobile.models.ShippingAddress;
import com.qa.mobile.screens.CartScreen;
import com.qa.mobile.screens.CatalogScreen;
import com.qa.mobile.screens.CheckoutCompleteScreen;
import com.qa.mobile.screens.LoginScreen;
import com.qa.mobile.screens.ReviewOrderScreen;
import com.qa.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

/**
 * The full purchase, identical on Android and iOS:
 * catalog -> product -> quantity -> cart -> login -> address -> payment -> review -> order complete.
 */
public class PurchaseFlowTest extends BaseTest {

    @DataProvider(name = "purchases")
    public Object[][] purchases() throws IOException {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("testdata/purchases.json")) {
            List<PurchaseScenario> rows = new ObjectMapper().readValue(in, new TypeReference<List<PurchaseScenario>>() { });
            return rows.stream().filter(PurchaseScenario::run).map(r -> new Object[] {r}).toArray(Object[][]::new);
        }
    }

    @Test(dataProvider = "purchases", groups = {"smoke", "regression", "e2e"},
            description = "Guest adds a product, logs in at checkout and places the order")
    public void guestCanBuyProduct(PurchaseScenario scenario) {
        Product product = scenario.toProduct();
        int qty = scenario.quantity();
        BigDecimal itemsTotal = product.price().multiply(BigDecimal.valueOf(qty));
        ShippingAddress address = ShippingAddress.sample();

        step("Add " + qty + " x " + product);
        CartScreen cart = catalog.openProduct(product).addToCart(qty).nav().openCart();
        Assert.assertEquals(cart.totalItems(), qty, "cart item count");
        Assert.assertEquals(cart.totalPrice(), itemsTotal, "cart total");

        step("Log in at checkout, then enter address and card");
        ReviewOrderScreen review = cart.proceedToCheckoutAsGuest()
                .loginForCheckout(LoginScreen.STANDARD_USER, LoginScreen.PASSWORD)
                .enterAddress(address)
                .enterCard(PaymentCard.sample());

        step("Review the order");
        String delivery = review.deliveryAddress();
        Assert.assertTrue(delivery.contains(address.fullName()), "Delivery address: " + delivery);
        Assert.assertTrue(delivery.contains(address.city()), "Delivery address: " + delivery);
        Assert.assertEquals(review.totalItems(), qty, "review item count");
        Assert.assertEquals(review.totalPrice(), itemsTotal.add(ReviewOrderScreen.DELIVERY_FEE), "review total incl. delivery");

        step("Place the order");
        CheckoutCompleteScreen complete = review.placeOrder();
        Assert.assertTrue(complete.showsConfirmation(), "Expected the 'Checkout Complete' message");

        CatalogScreen back = complete.continueShopping();
        Assert.assertEquals(back.nav().cartCount(), 0, "cart should be empty after ordering");
    }
}
