package com.qa.tests.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.qa.mobile.driver.DriverFactory;
import com.qa.mobile.reporting.ExtentManager;
import org.openqa.selenium.OutputType;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Arrays;
import java.util.stream.Collectors;

/** Adds every test to the HTML report and attaches a device screenshot on failure. */
public class TestListener implements ITestListener, ISuiteListener {

    @Override
    public void onTestStart(ITestResult result) {
        ExtentManager.start(displayName(result), result.getMethod().getDescription())
                .assignCategory(result.getMethod().getGroups());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentManager.current().pass("Passed");
        ExtentManager.end();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = ExtentManager.current();
        test.fail(result.getThrowable());
        // Failure callbacks run before @AfterMethod closes the session, so the device is still on the failing screen.
        if (DriverFactory.hasDriver()) {
            try {
                String png = DriverFactory.getDriver().getScreenshotAs(OutputType.BASE64);
                test.fail("Screen at failure", MediaEntityBuilder.createScreenCaptureFromBase64String(png).build());
            } catch (RuntimeException e) {
                test.warning("Could not capture screenshot: " + e.getMessage());
            }
        }
        ExtentManager.end();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = ExtentManager.current() != null
                ? ExtentManager.current()
                : ExtentManager.start(displayName(result), result.getMethod().getDescription());
        test.skip(result.getThrowable() == null ? "Skipped" : String.valueOf(result.getThrowable().getMessage()));
        ExtentManager.end();
    }

    @Override
    public void onFinish(ISuite suite) {
        ExtentManager.flush();
        System.out.println("[report] " + ExtentManager.REPORT_PATH);
    }

    private static String displayName(ITestResult result) {
        Object[] params = result.getParameters();
        String method = result.getMethod().getMethodName();
        if (params == null || params.length == 0) {
            return method;
        }
        return method + " [" + Arrays.stream(params).map(String::valueOf).collect(Collectors.joining(", ")) + "]";
    }
}
