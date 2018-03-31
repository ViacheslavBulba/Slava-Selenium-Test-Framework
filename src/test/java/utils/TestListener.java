package utils;

import com.aventstack.extentreports.MediaEntityBuilder;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestListener implements ITestListener{

  public void onTestStart(ITestResult iTestResult) {
    String testName;
    String firstPartOfTestName;
    Object[] parameters = iTestResult.getParameters();
    if(iTestResult.getMethod().getDescription() == null){
      firstPartOfTestName = iTestResult.getName();
    }
    else {
      firstPartOfTestName = iTestResult.getMethod().getDescription();
    }
    if(parameters.length == 0){
      testName = firstPartOfTestName;
    }
    else{
      StringBuilder builder = new StringBuilder();
      for (Object parameter : parameters){
        builder.append(parameter.toString() + ", ");
      }
      testName = firstPartOfTestName + " (" + builder.substring(0,builder.length()-2) + ")";
      }
    ReportHolder.createReport(testName);
  }

  public void onTestSuccess(ITestResult iTestResult){

  }

  public void onTestFailure(ITestResult iTestResult){
    String fileName = iTestResult.getName() + getDateAndTime();
    try{
      ReportHolder.getReport().fail(iTestResult.getThrowable(), MediaEntityBuilder.createScreenCaptureFromPath(BrowserHolder.getBrowser().getScreenshot(fileName)).build());
    } catch (IOException e){
      Logger.fail("ERROR WHILE TAKING A SCREENSHOT: " + e.getMessage());
    }
    BrowserHolder.getBrowser().getPageSource(fileName);
  }

  public void onTestSkipped(ITestResult iTestResult){
    ReportHolder.getReport().skip(iTestResult.getThrowable());
  }

  public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult){}

  public void onStart(ITestContext iTestContext){}

  public void onFinish(ITestContext iTestContext){}

  private String getDateAndTime(){
    Date dateNow = new Date();
    SimpleDateFormat formatForDateNow = new SimpleDateFormat("_yyyy_MM_dd_hh_mm_ss_SSS");
    return formatForDateNow.format(dateNow);
  }
}
