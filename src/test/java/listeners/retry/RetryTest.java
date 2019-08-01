package listeners.retry;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import utils.FileSystem;

public class RetryTest implements IRetryAnalyzer {

    private int retryCount = 0;
    private static final int ATTEMPTS = 2;

    public boolean retry(ITestResult result) {
        Object[] parameters = result.getParameters();
        String methodParameters = "";
        if (parameters.length != 0) {
            StringBuilder builder = new StringBuilder();
            for (Object parameter : parameters) {
                builder.append(parameter.toString() + ", ");
            }
            methodParameters = builder.substring(0, builder.length() - 2);
        }
        if (retryCount < ATTEMPTS && FileSystem.getPropertyFromConfigFile("seleniumGrid") != null) {
            System.err.println(
                String.format("Retrying Test: %s with parameters [%s], Attempt #%d", result.getMethod().getMethodName(),
                              methodParameters, (retryCount + 1)));
            retryCount++;
            return true;
        }
        return false;
    }

}
