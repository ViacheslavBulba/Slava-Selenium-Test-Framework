package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import static utils.FileSystem.fileSeparator;
import static utils.FileSystem.userDir;

public class Browser {

    protected WebDriver driver;
    private String url;
    private String seleniumGrid;
    private Duration timeout;
    private String browser;

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
        this.browser = FileSystem.getPropertyFromConfigFile("browser");
        this.seleniumGrid = System.getProperty("seleniumGrid") == null
                ? FileSystem.getPropertyFromConfigFile("seleniumGrid")
                : System.getProperty("seleniumGrid");
        System.out.println("seleniumGrid = " + this.seleniumGrid);
        try {
            this.timeout = Duration.ofSeconds(Long.parseLong(FileSystem.getPropertyFromConfigFile("timeout")));
        } catch (NumberFormatException nfe) {
            System.err.println(
                    "ERROR READING TIMEOUT VALUE FROM CONFIG\\TESTS.PROPERTIES FILE. SETTING UP DEFAULT VALUE 10 SECONDS.");
            this.timeout = Duration.ofSeconds(10L);
        }
        if (seleniumGrid == null) {
//            if (System.getProperty("os.name").toLowerCase().contains("mac")) {
//                System.setProperty("webdriver.chrome.driver",
//                        userDir + fileSeparator + "drivers" + fileSeparator + "chromedriver");
//            } else {
//                System.setProperty("webdriver.chrome.driver",
//                        userDir + fileSeparator + "drivers" + fileSeparator + "chromedriver.exe");
//            }
//            ChromeOptions options = new ChromeOptions();//work around for [SEVERE]: Timed out receiving message from renderer: 300.000
//            //AGRESSIVE: options.setPageLoadStrategy(PageLoadStrategy.NONE); // https://www.skptricks.com/2018/08/timed-out-receiving-message-from-renderer-selenium.html
//            options.addArguments("start-maximized"); // https://stackoverflow.com/a/26283818/1689770
//            options.addArguments("enable-automation"); // https://stackoverflow.com/a/43840128/1689770
//            options.addArguments("--headless"); // only if you are ACTUALLY running headless
//            options.addArguments("--no-sandbox"); //https://stackoverflow.com/a/50725918/1689770
//            options.addArguments("--disable-infobars"); //https://stackoverflow.com/a/43840128/1689770
//            options.addArguments("--disable-dev-shm-usage"); //https://stackoverflow.com/a/50725918/1689770
//            options.addArguments("--disable-browser-side-navigation"); //https://stackoverflow.com/a/49123152/1689770
//            options.addArguments("--disable-gpu"); //https://stackoverflow.com/questions/51959986/how-to-solve-selenium-chromedriver-timed-out-receiving-message-from-renderer-exc
//            this.driver = new ChromeDriver(options);

            switch (this.browser.toLowerCase()) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions chromeOptions = new ChromeOptions();
                    //chromeOptions.setPageLoadStrategy(PageLoadStrategy.NONE);
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    chromeOptions.addArguments("--disable-extensions");
                    chromeOptions.addArguments("--remote-allow-origins=*");
                    //chromeOptions.addArguments("--no-sandbox", "--disable-dev-shm-usage");
                    //chromeOptions.addArguments("--remote-debugging-port=9222");
                    // taskkill /im chromedriver.exe /f
                    this.driver = new ChromeDriver(chromeOptions);
                    break;
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    this.driver = new FirefoxDriver();
                    break;
                case "edge":
                    WebDriverManager.edgedriver().setup();
                    this.driver = new EdgeDriver();
                    break;
                case "safari":
                    WebDriverManager.safaridriver().setup();
                    this.driver = new SafariDriver();
                    break;
                default:
                    throw new RuntimeException(this.browser + " browser is not supported!");
            }
            this.driver.manage().timeouts().implicitlyWait(timeout);

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
        Logger.pass("Open page " + url);
        this.driver.manage().window().maximize();
        this.driver.get(url);
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

    public byte[] getScreenshotAsByteArray() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    public void getPageSource(String pageSourceName) {
        try {
            String folderString = userDir + fileSeparator + "pagesources";
            File folder = new File(folderString);
            if (!folder.exists()) {
                folder.mkdir();
            }
            String filePath = folderString + fileSeparator + pageSourceName + ".html";
            String content = driver.getPageSource();
            OutputStream file = new FileOutputStream(filePath);
            OutputStreamWriter writer = new OutputStreamWriter(file, StandardCharsets.UTF_8);
            writer.write(content);
            writer.close();
            if (System.getenv("BUILD_URL") == null) {
                filePath = folderString + fileSeparator + pageSourceName + ".html";
            } else {
                filePath =
                        System.getenv("BUILD_URL") + "artifact" + fileSeparator + "pagesources" + fileSeparator
                                + pageSourceName + ".html";
            }
        } catch (IOException e) {
            Logger.fail("ERROR WHILE TAKING PAGE SOURCE: " + e.getMessage());
            e.getMessage();
        }
    }

    public Duration getTimeout() {
        return this.timeout;
    }

    public Object executeScript(String js, Object... args) {
        JavascriptExecutor executor = (JavascriptExecutor) getWebDriver();
        try {
            return executor.executeScript(js, args);
        } catch (Exception e) {
            Logger.fail("COULD NOT EXECUTE JAVASCRIPT! " + e);
            return null;
        }
    }

}
