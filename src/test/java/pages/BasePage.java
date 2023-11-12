package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.Logger;
import utils.PageFactoryLayer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class BasePage extends PageFactoryLayer {

    WebDriverWait wait = new WebDriverWait(browser.getWebDriver(), browser.getTimeout());

    public int getNumberOfElements(String xpath) {
        List<WebElement> elements = browser.getWebDriver().findElements(By.xpath(xpath));
        return elements.size();
    }

    public boolean isElementPresent(String xpath) {
        if (getNumberOfElements(xpath) == 0) {
            return false;
        } else {
            return true;
        }
    }

    public void waitSeconds(int seconds) {
        Logger.info("Wait " + seconds + " second(s)");
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException ignore) {
        }
    }

    public boolean isElementWithTextEqualsPresent(String text) {
        List<WebElement> elements = browser.getWebDriver().findElements(By.xpath("//*[text()='" + text + "']"));
        if (elements.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public void refreshPage() {
        Logger.info("Refresh page");
        browser.getWebDriver().navigate().refresh();
    }

    private String getDateAndTime() {
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        return formatForDateNow.format(dateNow);
    }

    public void checkIfElementPresent(String xpath) {
        if (!isElementPresent(xpath)) {
            throw new RuntimeException("Cannot find element with xpath = " + xpath);
        }
    }

    public void enterText(String xpath, String text, String elementNameForReport) {
        Logger.pass("Enter text [" + text + "] in [" + elementNameForReport + "]");
        checkIfElementPresent(xpath);
        WebElement element = browser.getWebDriver().findElement(By.xpath(xpath));
        element.click();
        element.clear();
        element.sendKeys(text);
    }

    public void enterTextAndPressEnter(String xpath, String text, String elementNameForReport) {
        Logger.pass("Enter text [" + text + "] in [" + elementNameForReport + "] and press Enter");
        checkIfElementPresent(xpath);
        WebElement element = browser.getWebDriver().findElement(By.xpath(xpath));
        element.click();
        element.clear();
        element.sendKeys(text);
        element.sendKeys(Keys.ENTER);
    }

    public void scrollDown(int pixels) {
        Logger.pass("Scroll down [" + pixels + "] pixels");
        JavascriptExecutor js = (JavascriptExecutor) browser.getWebDriver();
        js.executeScript("window.scrollBy(0, " + pixels + ");");
    }

    public void scrollDownWithPause(int pixels, int times) {
        for (int i = 0; i < times; i++) {
            scrollDown(pixels);
            waitSeconds(1);
        }
    }

    public void clickByCoordinates(int xCoordinate, int yCoordinate) {
        Actions actions = new Actions(browser.getWebDriver());
        actions.moveToElement(browser.getWebDriver().findElement(By.tagName("body")), 0, 0);
        actions.moveByOffset(xCoordinate, yCoordinate).click().build().perform();
    }

    public void openPage(String url) {
        Logger.pass("Open page " + url);
        browser.getWebDriver().get(url);
    }

    public void clickOnElement(String xpath, String elementNameForReport) {
        Logger.pass("Click on [" + elementNameForReport + "]");
        checkIfElementPresent(xpath);
        browser.getWebDriver().findElement(By.xpath(xpath)).click();
    }

    public void clickOnText(String text) {
        Logger.pass("Click on text [" + text + "]");
        String xpath = "//*[contains(text(),'" + text + "')]";
        checkIfElementPresent(xpath);
        browser.getWebDriver().findElement(By.xpath(xpath)).click();
    }

    public String getTextFromElement(String xpath) {
        checkIfElementPresent(xpath);
        return browser.getWebDriver().findElement(By.xpath(xpath)).getText();
    }

    public String getValueFromInput(String xpath) { // inputs in html does not have text in regular meaning, they have value
        checkIfElementPresent(xpath);
        return browser.getWebDriver().findElement(By.xpath(xpath)).getAttribute("value");
    }

    public List<String> getTextFromElements(String xpath) {
        checkIfElementPresent(xpath);
        List<String> texts =
                browser.getWebDriver().findElements(By.xpath(xpath)).stream().map(WebElement::getText).collect(Collectors.toList());
        for (int i = 0; i < texts.size(); i++) {
            texts.set(i, texts.get(i).replace("\n", " "));
        }
        texts.forEach(System.out::println);
        return texts;
    }

    public void outputListToTerminal(List<String> list) {
        list.forEach(System.out::println);
    }

    public String generateRandomString(int maxLength) {
        String candidateChars = "qwertyuiopasdfghjklzxcvbnmABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        char randomLetter;
        for (int i = 0; i < random.nextInt(maxLength) + 1; i++) {
            randomLetter = candidateChars.charAt(random.nextInt(candidateChars.length()));
            result.append(randomLetter);
        }
        return result.toString();
    }

    public void assertEachElementContainsText(String xpath, String text, String elementNameForReport) {
        Logger.info("Verify that each [" + elementNameForReport + "] contains text [" + text + "]");
        List<String> texts = getTextFromElements(xpath);
        for (String value : texts) {
            assertTrue(value.toLowerCase().contains(text), "Value [" + value + "] does not contain text [" + text + "]");
        }
    }

    public void assertTextInElement(String xpath, String text, String elementNameForReport) {
        Logger.info("Verify that full text in [" + elementNameForReport + "] = [" + text + "]");
        assertEquals(getTextFromElement(xpath), text);
    }

    public void assertNumberOfElements(String xpath, int amount, String elementNameForReport) {
        Logger.info("Verify that number of [" + elementNameForReport + "] = [" + amount + "]");
        assertEquals(getNumberOfElements(xpath), amount);
    }

    public void assertTextIsPresentOnThePage(String text) {
        Logger.pass("Verify that text [" + text + "] is present on the page");
        String xpath = "//*[contains(text(),'" + text + "')]";
        assertTrue(getNumberOfElements(xpath) > 0, "Text [" + text + "] is not found");
    }
}
