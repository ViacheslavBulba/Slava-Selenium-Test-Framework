package listeners;

import com.aventstack.extentreports.MediaEntityBuilder;
import io.qameta.allure.Allure;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.BrowserHolder;
import utils.SingleExtentTestReportHolder;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class TestListener implements ITestListener {

    private Set<String> testNamesInTheReport = new HashSet<>();

    public static Set<String> failedTests = new HashSet<>();
    public static Set<String> skippedTests = new HashSet<>();

    public void onTestStart(ITestResult iTestResult) {
        SingleExtentTestReportHolder.createExtentTest(getTestName(iTestResult));
        testNamesInTheReport.add(getTestName(iTestResult));
    }

    public void onTestSuccess(ITestResult iTestResult) {
        failedTests.remove(getTestName(iTestResult));
    }

    public void onTestFailure(ITestResult iTestResult) {
        try {
            String currentUrl = BrowserHolder.getBrowser().getWebDriver().getCurrentUrl();
            String currentUrlLink = "<a href=\"" + currentUrl + "\" target=\"_blank\">" + currentUrl + "<a>";
            String fileName = iTestResult.getName() + getDateAndTime();
            String errorMessageToDisplay = iTestResult.getThrowable().getMessage();
            if (errorMessageToDisplay.contains("(Session info:")) {
                errorMessageToDisplay = errorMessageToDisplay.substring(0, errorMessageToDisplay.indexOf("(Session info:"));
            }
            if (errorMessageToDisplay.contains("Build info:")) {
                errorMessageToDisplay = errorMessageToDisplay.substring(0, errorMessageToDisplay.indexOf("Build info:"));
            }
            if (errorMessageToDisplay.contains("expected [true] but found [false]")) {
                errorMessageToDisplay = errorMessageToDisplay.substring(0, errorMessageToDisplay.indexOf("expected [true] but found [false]") - 1);
            }
            errorMessageToDisplay = errorMessageToDisplay.substring(0, 1).toUpperCase() + errorMessageToDisplay.substring(1);
            SingleExtentTestReportHolder.getExtentTest().fail(errorMessageToDisplay + "<br>" + currentUrlLink + "<br>",
                    MediaEntityBuilder.createScreenCaptureFromPath(BrowserHolder.getBrowser().getScreenshot(fileName))
                            .build());
            BrowserHolder.getBrowser().getPageSource(fileName);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if (errorMessage.contains("timed out receiving message from renderer")) {
                errorMessage = "Timed out receiving message from renderer. Browser got stuck.";
            }
            if (errorMessage.contains("Could not start a new session")) {
                errorMessage = "UnreachableBrowserException: Could not start a new session. Possible causes are invalid address of the remote server or browser start-up failure. Check if Selenium Grid is up and running.";
            }
            SingleExtentTestReportHolder.getExtentTest().fail(errorMessage);
        } finally {
            Allure.addAttachment("Screenshot from onTestFailure", new ByteArrayInputStream(BrowserHolder.getBrowser().getScreenshotAsByteArray()));
        }
        failedTests.add(getTestName(iTestResult));
    }

    public void onTestSkipped(ITestResult iTestResult) {
        if (!testNamesInTheReport.contains(getTestName(iTestResult))) {
            SingleExtentTestReportHolder.createExtentTest(getTestName(iTestResult));
            String messageToReport = iTestResult.getThrowable().getMessage();
            if (messageToReport.contains("depends on not successfully finished methods")){
                messageToReport = "Test was skipped as it depends on not successfully finished methods! Even though all other tests can be passed since we have retry in place.";
            }
            SingleExtentTestReportHolder.getExtentTest().skip(messageToReport);
            skippedTests.add(getTestName(iTestResult));
        }
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
    }

    public void onStart(ITestContext iTestContext) {
    }

    public void onFinish(ITestContext iTestContext) {
    }

    private String getDateAndTime() {
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("_yyyy_MM_dd_hh_mm_ss_SSS");
        return formatForDateNow.format(dateNow);
    }

    private String getTestName(ITestResult iTestResult) {
        String testName;
        String firstPartOfTestName;
        Object[] parameters = iTestResult.getParameters();
        if (iTestResult.getMethod().getDescription() == null) {
            firstPartOfTestName = iTestResult.getName();
        } else {
            firstPartOfTestName = iTestResult.getMethod().getDescription();
        }
        if (parameters.length == 0) {
            testName = firstPartOfTestName;
        } else {
            StringBuilder builder = new StringBuilder();
            for (Object parameter : parameters) {
                builder.append(parameter.toString() + ", ");
            }
            testName = firstPartOfTestName + " (" + builder.substring(0, builder.length() - 2) + ")";
        }
        return testName;
    }
}