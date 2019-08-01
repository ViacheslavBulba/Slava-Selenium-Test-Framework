package listeners;

import com.aventstack.extentreports.MediaEntityBuilder;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.BrowserHolder;
import utils.BrowserTestSuite;
import utils.Logger;
import utils.ReportHolder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestListener implements ITestListener {

    public void onTestStart(ITestResult iTestResult) {
        ReportHolder.createReport(getTestName(iTestResult));
    }

    public void onTestSuccess(ITestResult iTestResult) {

    }

    public void onTestFailure(ITestResult iTestResult) {
        String currentUrl = BrowserHolder.getBrowser().getWebDriver().getCurrentUrl();
        String currentUrlLink = "<a href=\"" + currentUrl + "\" target=\"_blank\">" + currentUrl + "<a>";
        String fileName = iTestResult.getName() + getDateAndTime();
        try {
            String errorMessageToDisplay = iTestResult.getThrowable().getMessage();
            if (errorMessageToDisplay.contains("(Session info:")){
                errorMessageToDisplay = errorMessageToDisplay.substring(0,errorMessageToDisplay.indexOf("(Session info:"));
            }
            if (errorMessageToDisplay.contains("Build info:")){
                errorMessageToDisplay = errorMessageToDisplay.substring(0,errorMessageToDisplay.indexOf("Build info:"));
            }
            if (errorMessageToDisplay.contains("expected [true] but found [false]")){
                errorMessageToDisplay = errorMessageToDisplay.substring(0, errorMessageToDisplay.indexOf("expected [true] but found [false]") - 1);
            }
            ReportHolder.getReport().fail(errorMessageToDisplay + "<br>" + currentUrlLink + "<br>",
                                          MediaEntityBuilder.createScreenCaptureFromPath(
                                              BrowserHolder.getBrowser().getScreenshot(fileName)).build());
        } catch (IOException e) {
            Logger.fail("ERROR WHILE TAKING A SCREENSHOT: " + e.getMessage());
        }
        BrowserHolder.getBrowser().getPageSource(getTestName(iTestResult) + getDateAndTime());
    }

    public void onTestSkipped(ITestResult iTestResult) {
        //ReportHolder.getReport().skip(iTestResult.getThrowable());
        BrowserTestSuite.extent.removeTest(ReportHolder.getReport());
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
