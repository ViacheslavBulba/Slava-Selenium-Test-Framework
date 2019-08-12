package utils;

import static utils.FileSystem.fileSeparator;
import static utils.FileSystem.userDir;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class Browser {

    protected WebDriver driver;
    private String url;
    private String seleniumGrid;
    private long timeout;

    public Browser() {
        this.setUp();
        this.openHost();
    }

    public WebDriver getWebDriver() {
        if (this.driver == null) {
            this.setUp();
        }
        return this.driver;
    }

    public String getUrl() {
        if (this.url == null) {
            this.setUp();
        }
        return this.url;
    }

    public void setUp() {
        this.url = FileSystem.getPropertyFromConfigFile("url");
        this.seleniumGrid = FileSystem.getPropertyFromConfigFile("seleniumGrid");
        try {
            this.timeout = Long.parseLong(FileSystem.getPropertyFromConfigFile("timeout"));
        } catch (NumberFormatException nfe) {
            System.err.println(
                "ERROR READING TIMEOUT VALUE FROM CONFIG\\TEST.PROPERTIES FILE. SETTING UP DEFAULT VALUE 10 SECONDS.");
            this.timeout = 10L;
        }
        if (seleniumGrid == null) {
            System.setProperty("webdriver.chrome.driver",
                               userDir + fileSeparator + "drivers" + fileSeparator + "chromedriver.exe");
            this.driver = new ChromeDriver();
            this.driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
        } else {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setBrowserName("chrome");
            capabilities.setPlatform(Platform.ANY);
            try {
                this.driver = new RemoteWebDriver(new URL(seleniumGrid), capabilities);
            } catch (MalformedURLException e) {
                Logger.fail("MALFORMED SELENIUM GRID URL");
                e.printStackTrace();
            }
        }
    }

    public void openHost() {
        Logger.pass("Opening page " + url);
        this.driver.get(url);
        this.driver.manage().window().maximize();
    }

    public void quit() {
        driver.quit();
        driver = null;
    }

    public String getScreenshot(String screenshotName) {
        try {
            String folderString = userDir + fileSeparator + "screenshots";
            File folder = new File(folderString);
            if (!folder.exists()) {
                folder.mkdir();
            }
            String filePath = folderString + fileSeparator + screenshotName + ".png";
            TakesScreenshot screenshotGetter = (TakesScreenshot) driver;
            File source = screenshotGetter.getScreenshotAs(OutputType.FILE);
            File file = new File(filePath);
            FileUtils.copyFile(source, file);
            if (System.getenv("BUILD_URL") == null) {
                filePath = folderString + fileSeparator + screenshotName + ".png";
            } else {
                filePath =
                    System.getenv("BUILD_URL") + "artifact" + fileSeparator + "screenshots" + fileSeparator
                    + screenshotName + ".png";
            }
            return filePath;
        } catch (IOException e) {
            Logger.fail("ERROR WHILE TAKING A SCREENSHOT: " + e.getMessage());
            return e.getMessage();
        }
    }

    public String getPageSource(String pageSourceName) {
        try {
            String folderString = userDir + fileSeparator + "pagesources";
            File folder = new File(folderString);
            if (!folder.exists()) {
                folder.mkdir();
            }
            String filePath = folderString + fileSeparator + pageSourceName + ".html";
            String content = driver.getPageSource();
            OutputStream fileOutputStream = new FileOutputStream(new File(filePath));
            OutputStreamWriter swriter = new OutputStreamWriter(fileOutputStream, "UTF8");
            swriter.write(content);
            swriter.close();
            if (System.getenv("BUILD_URL") == null) {
                filePath = folderString + fileSeparator + pageSourceName + ".html";
            } else {
                filePath =
                    System.getenv("BUILD_URL") + "artifact" + fileSeparator + "pagesources" + fileSeparator
                    + pageSourceName + ".html";
            }
            return filePath;
        } catch (IOException e) {
            Logger.fail("ERROR WHILE TAKING A PAGE SOURCE: " + e.getMessage());
            return e.getMessage();
        }
    }

    public Long getTimeout() {
        return this.timeout;
    }

    public Object executeScript(String js, Object... args) {
        JavascriptExecutor executor = (JavascriptExecutor) getWebDriver();
        try {
            return executor.executeScript(js, args);
        } catch (Exception e) {
            Logger.fail("COULD NOT EXECUTE JAVASCRIPT! " + e.toString());
            return null;
        }
    }

}
