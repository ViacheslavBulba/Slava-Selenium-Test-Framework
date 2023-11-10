package utils;

import static utils.FileSystem.fileSeparator;
import static utils.FileSystem.userDir;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ReportManager {

    private static ExtentReports extent;
    public static String htmlReportTitle = "Test Run Report";

    public static void setReportTitle(String reportTitle) {
        htmlReportTitle = reportTitle;
    }

    public static String getReportTitle() {
        return htmlReportTitle;
    }

    public static ExtentReports getInstance(String reportName) {
        if (extent == null) {
            createInstance(reportName);
        }
        return extent;
    }

    private static ExtentReports createInstance(String reportName) {
        String folderString = userDir + fileSeparator + "logs";
        File folder = new File(folderString);
        if (!folder.exists()) {
            folder.mkdir();
        }
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("EEEEE, MMMMM dd, yyyy, hh-mm a zzzz");
        String filePath = folderString + fileSeparator + "Test Run on " + formatForDateNow.format(dateNow) + ".html";
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(filePath);
        htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
        htmlReporter.config().setChartVisibilityOnOpen(true);
        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().setDocumentTitle(htmlReportTitle);
        htmlReporter.config().setEncoding("utf-8");
        htmlReporter.config().setReportName(reportName);
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        return extent;
    }
}
