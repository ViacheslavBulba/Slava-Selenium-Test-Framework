package utils;

import static utils.FileSystem.clearFolder;

import com.aventstack.extentreports.ExtentReports;
import org.testng.ITestContext;
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
        extent = ReportManager.getInstance(iTestContext.getSuite().getName());
    }

    @BeforeMethod
    public void createBrowser() {
        BrowserHolder.setBrowserCreated(false);
    }

    @AfterMethod(alwaysRun = true)
    public void browserQuit() {
        if (BrowserHolder.isBrowserCreated()) {
            BrowserHolder.getBrowser().quit();
        }
        BrowserHolder.setBrowser(null);
    }

    @AfterSuite(alwaysRun = true)
    public void recordAllDataToReport() {
        extent.flush();
    }


}
