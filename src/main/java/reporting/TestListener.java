package reporting;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.io.FileHandler;

import java.io.File;
import java.io.IOException;

public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        Logs.info("Test started: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        Logs.info("Test passed: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Logs.error("Test failed: " + result.getName());
        WebDriver driver = (WebDriver) result.getTestContext().getAttribute("driver");
        takeScreenshot(result.getName(), driver);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        Logs.warn("Test skipped: " + result.getName());
    }

    @Override
    public void onStart(ITestContext context) {
        Logs.info("Test suite started: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        Logs.info("Test suite finished: " + context.getName());
    }

    public void takeScreenshot(String fileName, WebDriver driver) {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileHandler.copy(screenshot, new File("screenshots/" + fileName + ".png"));
        } catch (IOException e) {
            Logs.error("Failed to take screenshot: " + e.getMessage());
        }
    }
}