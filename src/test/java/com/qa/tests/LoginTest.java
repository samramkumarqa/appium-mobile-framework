package com.qa.tests;

import com.qa.mobile.screens.CatalogScreen;
import com.qa.mobile.screens.LoginScreen;
import com.qa.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.qa.mobile.screens.LoginScreen.LOCKED_OUT_USER;
import static com.qa.mobile.screens.LoginScreen.PASSWORD;
import static com.qa.mobile.screens.LoginScreen.STANDARD_USER;

public class LoginTest extends BaseTest {

    @Test(groups = {"smoke", "regression"}, description = "Standard user logs in from the menu")
    public void validLoginReturnsToCatalog() {
        CatalogScreen afterLogin = catalog.nav().openLoginFromMenu().loginFromMenu(STANDARD_USER, PASSWORD);
        Assert.assertTrue(afterLogin.isShown());
    }

    @DataProvider(name = "invalidLogins")
    public Object[][] invalidLogins() {
        return new Object[][] {
                // username, password, which error, expected message
                {"", PASSWORD, "username", "Username is required"},
                {STANDARD_USER, "", "password", "Password is required"},
                {STANDARD_USER, "wrong-password", "generic", "Provided credentials do not match any user in this service."},
                {LOCKED_OUT_USER, PASSWORD, "generic", "Sorry, this user has been locked out."},
        };
    }

    @Test(dataProvider = "invalidLogins", groups = "regression", description = "Invalid logins show the right error")
    public void invalidLoginShowsError(String user, String pass, String errorField, String expected) {
        LoginScreen login = catalog.nav().openLoginFromMenu().loginExpectingError(user, pass);

        String actual = switch (errorField) {
            case "username" -> login.usernameError();
            case "password" -> login.passwordError();
            default -> login.genericError();
        };
        Assert.assertEquals(actual, expected);
    }
}
