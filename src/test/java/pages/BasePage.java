package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
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
        // Logger.info("Wait " + seconds + " second(s)");
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
        Logger.pass("Scroll down [" + pixels + "] pixels [" + times + "] times");
        for (int i = 0; i < times; i++) {
            JavascriptExecutor js = (JavascriptExecutor) browser.getWebDriver();
            js.executeScript("window.scrollBy(0, " + pixels + ");");
            waitSeconds(1);
        }
    }

    public void clickByCoordinates(int xCoordinate, int yCoordinate) {
        Logger.pass("Click by coordinates [" + xCoordinate + "][" + yCoordinate + "]");
        Actions actions = new Actions(browser.getWebDriver());
        actions.moveToElement(browser.getWebDriver().findElement(By.tagName("body")), 0, 0);
        actions.moveByOffset(xCoordinate, yCoordinate).click().build().perform();
    }

    public void movePointerToElement(String xpath, String elementNameForReport) {
        Logger.pass("Move pointer to [" + elementNameForReport + "]");
        checkIfElementPresent(xpath);
        WebElement element = browser.getWebDriver().findElement(By.xpath(xpath));
        Dimension size = element.getSize();
        int xOffset = size.getWidth() / 2;
        int yOffset = size.getHeight() / 2;
        Actions actions = new Actions(browser.getWebDriver());
        actions.moveToElement(element, xOffset, yOffset).perform();
    }

    public void movePointerToText(String text) {
        Logger.pass("Move pointer to text [" + text + "]");
        String xpath = "//*[contains(text(),'" + text + "')]";
        checkIfElementPresent(xpath);
        WebElement element = browser.getWebDriver().findElement(By.xpath(xpath));
        Dimension size = element.getSize();
        int xOffset = size.getWidth() / 2;
        int yOffset = size.getHeight() / 2;
        Actions actions = new Actions(browser.getWebDriver());
        actions.moveToElement(element, xOffset, yOffset).perform();
    }

    public void selectOptionInDropdown(String dropdownSelectXpath, String optionText, String dropdownNameForReport) {
        Logger.pass("Select option [" + optionText + "] in [" + dropdownNameForReport + "] dropdown");
        Select select = new Select(browser.getWebDriver().findElement(By.xpath(dropdownSelectXpath)));
        select.selectByVisibleText(optionText);
    }

    public void openPage(String url) {
        Logger.pass("Open page " + url);
        browser.getWebDriver().get(url);
    }

    public String getPageUrl() {
        String url = browser.getWebDriver().getCurrentUrl();
        Logger.pass("Page url: " + url);
        return url;
    }

    public String getPageTitle() {
        String title = browser.getWebDriver().getTitle();
        Logger.pass("Page title: " + title);
        return title;
    }

    public void clickOnElement(String xpath, String elementNameForReport) {
        Logger.pass("Click on [" + elementNameForReport + "]");
        checkIfElementPresent(xpath);
        browser.getWebDriver().findElement(By.xpath(xpath)).click();
    }

    public void clickOnNthFromSameElements(String xpath, int n, String elementNameForReport) {
        Logger.pass("Click on " + n + "-nth [" + elementNameForReport + "]");
        checkIfElementPresent(xpath);
        browser.getWebDriver().findElements(By.xpath(xpath)).get(n - 1).click();
    }

    public void clickOnText(String text) {
        Logger.pass("Click on text [" + text + "]");
        String xpath = "//*[contains(text(),'" + text + "')]";
        checkIfElementPresent(xpath);
        browser.getWebDriver().findElement(By.xpath(xpath)).click();
    }

    public void clickOnLink(String text) {
        Logger.pass("Click on link [" + text + "]");
        String xpath = "//*[contains(text(),'" + text + "')]";
        checkIfElementPresent(xpath);
        browser.getWebDriver().findElement(By.xpath(xpath)).click();
    }

    public String getTextFromElement(String xpath) {
        checkIfElementPresent(xpath);
        return browser.getWebDriver().findElement(By.xpath(xpath)).getText();
    }

    public String getAttributeFromElement(String xpath, String attr) {
        checkIfElementPresent(xpath);
        return browser.getWebDriver().findElement(By.xpath(xpath)).getAttribute(attr);
    }

    public String getValueFromInput(String xpath) { // inputs in html does not have text in regular meaning, they have value
        checkIfElementPresent(xpath);
        return browser.getWebDriver().findElement(By.xpath(xpath)).getAttribute("value");
    }

    public void assertValueInInput(String xpath, String text, String elementNameForReport) {
        Logger.info("Verify that text in [" + elementNameForReport + "] = [" + text + "]");
        assertEquals(getValueFromInput(xpath), text);
    }

    public List<String> getTextFromElements(String xpath) {
        checkIfElementPresent(xpath);
        List<String> texts = browser.getWebDriver().findElements(By.xpath(xpath)).stream().map(WebElement::getText).collect(Collectors.toList());
        for (int i = 0; i < texts.size(); i++) {
            texts.set(i, texts.get(i).replace("\n", " "));
        }
        texts.forEach(System.out::println);
        return texts;
    }

    public void outputListToTerminal(List<String> list) {
        list.forEach(System.out::println);
    }

    public void outputToTerminal(String text) {
        Logger.info(text);
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

    public void assertTextIsPresent(String text) {
        Logger.pass("Verify that text [" + text + "] is present on the page");
        String xpath = "//*[contains(text(),'" + text + "')]";
        assertTrue(getNumberOfElements(xpath) > 0, "Text [" + text + "] is not found");
    }

    public void assertTextIsMissing(String text) {
        Logger.pass("Verify that text [" + text + "] is missing on the page");
        String xpath = "//*[contains(text(),'" + text + "')]";
        assertTrue(getNumberOfElements(xpath) == 0, "Text [" + text + "] should not be displayed");
    }

    public void assertElementIsPresent(String xpath, String elementNameForReport) {
        Logger.pass("Verify that [" + elementNameForReport + "] is present on the page");
        assertTrue(getNumberOfElements(xpath) > 0, "[" + elementNameForReport + "] is not found");
    }

    public void assertElementIsMissing(String xpath, String elementNameForReport) {
        Logger.pass("Verify that [" + elementNameForReport + "] is missing on the page");
        assertTrue(getNumberOfElements(xpath) == 0, "[" + elementNameForReport + "] should not be displayed");
    }

    public void assertPageUrl(String urlPart) {
        Logger.pass("Verify that page URL contains [" + urlPart + "]");
        assertTrue(browser.getWebDriver().getCurrentUrl().toLowerCase().contains(urlPart.toLowerCase()), "Page Url does not contain [" + urlPart + "]");
    }

    public void assertPageTitle(String title) {
        Logger.pass("Verify that page title is [" + title + "]");
        assertEquals(getPageTitle(), title, "Page title is not [" + title + "]");
    }

    public void assertElementIsSelected(String xpath, String elementNameForReport) {
        Logger.pass("Verify that [" + elementNameForReport + "] is selected");
        assertTrue(browser.getWebDriver().findElement(By.xpath(xpath)).isSelected(), "[" + elementNameForReport + "] is not selected");
    }

    public void assertStringsEquals(String value1, String value2) {
        Logger.pass("Verify that [" + value1 + "] = [" + value2 + "]");
        assertEquals(value1, value2, "[" + value1 + "] is not the same as [" + value2 + "]");
    }
}
