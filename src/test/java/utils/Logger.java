package utils;

import com.aventstack.extentreports.Status;

public class Logger {

    public static void pass(String text) {
        ReportHolder.getReport().log(Status.PASS, text);
    }

    public static void info(String text) {
        ReportHolder.getReport().log(Status.INFO, text);
    }

}
