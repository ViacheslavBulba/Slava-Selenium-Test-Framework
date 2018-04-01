package utils;

import static utils.FileSystem.fileSeparator;
import static utils.FileSystem.userDir;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;


public class ReportManager {

private static ExtentReports extent;
  static final String HTML_REPORT_TITLE = "Test Run Report";

  public static ExtentReports getInstance(String reportName) {
    if (extent == null)
      createInstance(reportName);
    return extent;
  }

  public static ExtentReports createInstance(String reportName) {
    String folderString = userDir + fileSeparator + "logs";
    File folder = new File(folderString);
    if (!folder.exists()) {
      folder.mkdir();
    }
    String filePath = folderString + fileSeparator + "log.html";
    ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(filePath);
    htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
    htmlReporter.config().setChartVisibilityOnOpen(true);
    htmlReporter.config().setTheme(Theme.STANDARD);
    htmlReporter.config().setDocumentTitle(HTML_REPORT_TITLE);
    htmlReporter.config().setEncoding("utf-8");
    htmlReporter.config().setReportName(reportName);
    extent = new ExtentReports();
    extent.attachReporter(htmlReporter);
    return extent;
  }
}
