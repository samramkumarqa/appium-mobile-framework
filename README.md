# Appium Mobile Framework

[![Android Tests](https://github.com/samramkumarqa/appium-mobile-framework/actions/workflows/android.yml/badge.svg)](https://github.com/samramkumarqa/appium-mobile-framework/actions/workflows/android.yml)

Cross-platform mobile test automation with **Appium**, **Java 17** and **TestNG**.
One set of screen objects drives the same end-to-end purchase on **Android** and **iOS**, using Sauce Labs' open-source [My Demo App](https://github.com/saucelabs/my-demo-app-rn).

The headline test: browse the catalog → open a product → set quantity → add to cart → check out as a guest → log in → enter shipping address → enter card → review the order (totals include delivery) → place the order → confirm "Checkout Complete" and an empty cart.

## Highlights

| Area | What it shows |
| --- | --- |
| One codebase, two platforms | Screens locate elements by accessibility id, which the app sets identically on Android and iOS; only the cart/menu entry points differ |
| Screen Object Model | One class per screen with fluent navigation (`catalog.openProduct(p).addToCart(2).nav().openCart()`) |
| Data-driven | Purchase scenarios from `testdata/purchases.json`; products and prices modelled as an enum |
| Real assertions | Cart badge, item counts, line totals, review total incl. delivery fee, delivery address, error messages |
| Device-independent gestures | Scrolling uses Appium's `mobile: scrollGesture` / `mobile: swipe` on the scroll view, not fixed screen coordinates |
| Clean state | A fresh session per test, so tests never depend on each other |
| Modern Appium | Appium 2+/java-client 9, `UiAutomator2Options` / `XCUITestOptions`, W3C actions (no deprecated `TouchAction`) |
| Reporting | ExtentReports HTML with a device screenshot on every failure |
| CI | GitHub Actions boots an Android emulator, starts Appium and runs the suite on every push |

## Test coverage

| Class | Tests |
| --- | --- |
| `PurchaseFlowTest` | Full guest checkout with login, one run per data row |
| `CartTest` | Badge and totals across products, removing the last item |
| `ProductTest` | Name, price and default quantity for first, middle and last catalog items |
| `LoginTest` | Valid login; missing username, missing password, wrong password, locked-out user |

## Project structure

```
src/main/java/com/qa/mobile/
├── config/      Config (properties → env vars → -D flags)
├── driver/      DriverFactory (Android/iOS options), Platform
├── screens/     BaseScreen + one class per screen, NavBar
├── models/      Product, PurchaseScenario, ShippingAddress, PaymentCard
├── reporting/   ExtentManager
└── utils/       Money (price parsing)
src/test/java/com/qa/tests/      Tests, BaseTest, TestListener
src/test/resources/              config.properties, testdata/, suites/
apps/                            App builds (downloaded, not committed)
```

## Running

Full setup for Android and iOS, including troubleshooting: [docs/local-setup.md](docs/local-setup.md). In short:

```bash
# 1. Put the app in apps/ (see apps/README.md), start an emulator and run `appium`
# 2. Then:
mvn test                                              # Android, full regression
mvn test -Dsuite=smoke                                # smoke group
mvn test -Dplatform=ios -Dios.device.name="iPhone 15" # iOS simulator
```

Report: `target/extent-report/index.html`.
