package utils;

import org.testng.ISuite;
import org.testng.ISuiteListener;

public class SuiteListener implements ISuiteListener {

    @Override
    public void onStart(ISuite iSuite) {
        ReportManager.setReportTitle(iSuite.getName());
    }

    @Override
    public void onFinish(ISuite iSuite) {

    }
}
