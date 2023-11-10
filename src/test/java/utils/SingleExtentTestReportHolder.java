package utils;

import com.aventstack.extentreports.ExtentTest;

public class SingleExtentTestReportHolder {

    private static ThreadLocal<ExtentTest> singleExtentTestReportHolder = new ThreadLocal();

    public SingleExtentTestReportHolder() {
    }

    public static ExtentTest createExtentTest(String name) {
        ExtentTest test = BrowserTestSuite.extent.createTest(name);
        singleExtentTestReportHolder.set(test);
        return test;
    }

    public static ExtentTest getExtentTest() {
        ExtentTest test = singleExtentTestReportHolder.get();
        return test;
    }

}
