package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.PageFactoryLayer;

import java.util.List;

public class BasePage extends PageFactoryLayer {

    WebDriverWait wait = new WebDriverWait(browser.getWebDriver(), browser.getTimeout());

    public boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isElementPresent(String cssLocator) {
        List<WebElement> elements = browser.getWebDriver().findElements(By.cssSelector(cssLocator));
        if (elements.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public void sleep(int seconds) {
        System.out.println("sleep " + seconds + " second(s)");
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException ignore) {
        }
    }
}
