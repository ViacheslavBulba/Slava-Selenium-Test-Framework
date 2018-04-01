package utils;

import com.aventstack.extentreports.ExtentTest;

public class ReportHolder {

    private static ThreadLocal<ExtentTest> reportHolder = new ThreadLocal();

    public ReportHolder() {
    }

    public static ExtentTest createReport(String name) {
        ExtentTest test = BrowserTestSuite.extent.createTest(name);
        reportHolder.set(test);
        return test;
    }

    public static ExtentTest getReport() {
        ExtentTest test = reportHolder.get();
        return test;
    }

}
