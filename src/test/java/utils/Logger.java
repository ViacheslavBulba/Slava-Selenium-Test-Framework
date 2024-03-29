package utils;

import com.aventstack.extentreports.Status;
import io.qameta.allure.Allure;

public class Logger {

    public static void pass(String text) {
        System.out.println(text);
        Allure.step(text);
        try {
            SingleExtentTestReportHolder.getExtentTest().log(Status.PASS, text);
        } catch (NullPointerException ignore) {
        }
    }

    public static void info(String text) {
        System.out.println(text);
        Allure.step(text);
        try {
            SingleExtentTestReportHolder.getExtentTest().log(Status.INFO, text);
        } catch (NullPointerException ignore) {
        }
    }

    public static void fail(String text) {
        System.out.println(text);
        Allure.step(text);
        try {
            SingleExtentTestReportHolder.getExtentTest().log(Status.FAIL, text);
        } catch (NullPointerException ignore) {
        }
    }

}
