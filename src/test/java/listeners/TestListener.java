package listeners;

import com.aventstack.extentreports.MediaEntityBuilder;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.BrowserHolder;
import utils.SingleExtentTestReportHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class TestListener implements ITestListener {

    private Set<String> testNamesInTheReport = new HashSet<>();

    public void onTestStart(ITestResult iTestResult) {
        SingleExtentTestReportHolder.createExtentTest(getTestName(iTestResult));
        testNamesInTheReport.add(getTestName(iTestResult));
    }

    public void onTestSuccess(ITestResult iTestResult) {
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
            SingleExtentTestReportHolder.getExtentTest().fail(errorMessageToDisplay + "<br>" + currentUrlLink + "<br>",
                    MediaEntityBuilder.createScreenCaptureFromPath(BrowserHolder.getBrowser().getScreenshot(fileName))
                            .build());
            BrowserHolder.getBrowser().getPageSource(fileName);
        } catch (Exception e) {
            SingleExtentTestReportHolder.getExtentTest().fail(
                    "Severe test failure, probably timed out receiving message from renderer: 300.000");//fail the test in any case
        }
    }

    public void onTestSkipped(ITestResult iTestResult) {
        if (!testNamesInTheReport.contains(getTestName(iTestResult))) {
            SingleExtentTestReportHolder.createExtentTest(getTestName(iTestResult));
            SingleExtentTestReportHolder.getExtentTest().skip(iTestResult.getThrowable().getMessage());
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

    public String getTestName(ITestResult iTestResult) {
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