package utils;

import static listeners.TestListener.failedTests;
import static listeners.TestListener.skippedTests;
import static utils.FileSystem.clearFolder;

import com.aventstack.extentreports.ExtentReports;
import org.testng.ITestContext;
import org.testng.TestException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

public class BrowserTestSuite {

    public static ExtentReports extent;

    public BrowserTestSuite() {
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

    @AfterMethod(alwaysRun = true)
    public void browserQuit() {
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

}