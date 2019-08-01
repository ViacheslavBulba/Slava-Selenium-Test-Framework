package listeners.retry;

import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import utils.ReportManager;

import java.util.Map;
import java.util.Set;

public class RetryTestSuiteListener implements ISuiteListener {

    @Override
    public void onStart(ISuite iSuite) {
        ReportManager.setReportTitle(iSuite.getName());
    }

    @Override
    public void onFinish(ISuite iSuite) {
        Map<String, ISuiteResult> results = iSuite.getResults();
        for (Map.Entry<String, ISuiteResult> result : results.entrySet()) {
            ITestContext testContext = result.getValue().getTestContext();
            removeDuplicates(testContext);
        }
    }

    private void removeDuplicates(ITestContext context) {
        Set<ITestResult> skippedTests = context.getSkippedTests().getAllResults();
        for (ITestResult skippedTest : skippedTests) {
            String skippedTestMethodName = skippedTest.getMethod().getMethodName();
            String skippedTestClassName = skippedTest.getMethod().getTestClass().getName();
            String skippedTestParameters = getParameters(skippedTest.getParameters());
            Set<ITestResult> failedTests = context.getFailedTests().getAllResults();
            for (ITestResult failedTest : failedTests) {
                String failedTestMethodName = failedTest.getMethod().getMethodName();
                String failedTestClassName = failedTest.getMethod().getTestClass().getName();
                String failedTestParameters = getParameters(failedTest.getParameters());
                if (skippedTestMethodName.equals(failedTestMethodName) && skippedTestClassName
                    .equals(failedTestClassName) && skippedTestParameters.equals(failedTestParameters)) {
                    skippedTests.remove(skippedTest);
                }
            }
            Set<ITestResult> passedTests = context.getPassedTests().getAllResults();
            for (ITestResult passedTest : passedTests) {
                String passedTestMethodName = passedTest.getMethod().getMethodName();
                String passedTestClassName = passedTest.getMethod().getTestClass().getName();
                String passedTestParameters = getParameters(passedTest.getParameters());
                if (skippedTestMethodName.equals(passedTestMethodName) && skippedTestClassName
                    .equals(passedTestClassName) && skippedTestParameters.equals(passedTestParameters)) {
                    skippedTests.remove(skippedTest);
                }
            }
        }
        Set<ITestResult> failedTests = context.getFailedTests().getAllResults();
        for (ITestResult failedTest : failedTests) {
            String failedTestMethodName = failedTest.getMethod().getMethodName();
            String failedTestClassName = failedTest.getMethod().getTestClass().getName();
            String failedTestParameters = getParameters(failedTest.getParameters());
            Set<ITestResult> passedTests = context.getPassedTests().getAllResults();
            for (ITestResult passedTest : passedTests) {
                String passedTestMethodName = passedTest.getMethod().getMethodName();
                String passedTestClassName = passedTest.getMethod().getTestClass().getName();
                String passedTestParameters = getParameters(passedTest.getParameters());
                if (failedTestMethodName.equals(passedTestMethodName) && failedTestClassName.equals(passedTestClassName)
                    && failedTestParameters.equals(passedTestParameters)) {
                    failedTests.remove(failedTest);
                }
            }
        }
    }

    private String getParameters(Object[] params) {
        String parametersInString = "";
        if (params.length != 0) {
            StringBuilder builder = new StringBuilder();
            for (Object parameter : params) {
                if (parameter != null) {
                    builder.append(parameter.toString() + ", ");
                } else {
                    builder.append("EMPTY, ");
                }
            }
            parametersInString = builder.substring(0, builder.length() - 2);
        }
        return parametersInString;
    }
}
