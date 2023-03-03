package utils;

import com.aventstack.extentreports.ExtentReports;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import pages.BasePage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static listeners.TestListener.failedTests;
import static listeners.TestListener.skippedTests;
import static org.testng.Assert.assertTrue;
import static utils.FileSystem.clearFolder;

public class BrowserTestSuite {

    public static ExtentReports extent;

    public BrowserTestSuite() {
    }

    public static String generateRandomString(int maxLength) {
        String candidateChars = "qwertyuiopasdfghjklzxcvbnmABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        char randomLetter;
        for (int i = 0; i < random.nextInt(maxLength) + 1; i++) {
            randomLetter = candidateChars.charAt(random.nextInt(candidateChars.length()));
            result.append(randomLetter);
        }
        return result.toString();
    }

    @BeforeSuite
    public void createReport(ITestContext iTestContext) {
        clearFolder("logs");
        clearFolder("screenshots");
        clearFolder("pagesources");
        clearFolder("allure-results");
        //ReportManager.setReportTitle(iTestContext.getSuite().getName());//you can set html report title here
        extent = ReportManager.getInstance(iTestContext.getSuite().getName());
    }

    @BeforeMethod
    public void createBrowser() {
        BrowserHolder.setBrowserCreated(false);
    }

    private void takeScreenshotIfFailed(ITestResult result) {
        if (!result.isSuccess()) {
            String fileName = result.getName() + getDateAndTime();
            BrowserHolder.getBrowser().getScreenshot(fileName);
        }
    }

    private void takePageSourceIfFailed(ITestResult result) {
        if (!result.isSuccess()) {
            String fileName = result.getName() + getDateAndTime();
            BrowserHolder.getBrowser().getPageSource(fileName);
        }
    }

    @AfterMethod(alwaysRun = true)
    public void browserQuit(ITestResult result) {
        try {
            takeScreenshotIfFailed(result); // take screenshots when running via IDE from class directly (without listeners)
            takePageSourceIfFailed(result);
        } catch (Exception | Error ignore) {
            System.out.println("ERROR TAKING PAGE SOURCE AND SCREENSHOT");
        }
        // Allure.addAttachment("my attachment", "string test");
        // Allure.addAttachment("Screenshot from browserQuit", new ByteArrayInputStream(BrowserHolder.getBrowser().getScreenshotAsByteArray()));
        if (BrowserHolder.isBrowserCreated()) {
            BrowserHolder.getBrowser().quit();
        }
        BrowserHolder.setBrowser(null);
    }

    @AfterSuite(alwaysRun = true)
    public void recordAllDataToReport() {
        extent.flush();
    }

    @AfterSuite(alwaysRun = true)
    public void createTestFailureIfThereAreOnlySuccessAndSkippedTests() {
        if (failedTests.size() == 0 && skippedTests.size() > 0) {
            throw new TestException("There are skipped tests");
        }
    }

    public void assertTextIsPresent(BasePage page, String text) {
        Logger.info("Check that the following text is displayed: " + text);
        assertTrue(page.isElementWithTextEqualsPresent(text), "Cannot find text [" + text + "] on the page");
    }

    private String getDateAndTime() {
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("_yyyy_MM_dd_hh_mm_ss_SSS");
        return formatForDateNow.format(dateNow);
    }

}