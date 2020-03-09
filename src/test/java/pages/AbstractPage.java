package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.PageFactoryLayer;

import java.util.List;

public class AbstractPage extends PageFactoryLayer {

    WebDriverWait wait = new WebDriverWait(browser.getWebDriver(), browser.getTimeout());
    WebDriverWait waitLonger = new WebDriverWait(browser.getWebDriver(), browser.getTimeout()*3);

    String regExpForExact7or40Symbols = "^(?=.{7}$).*|^(?=.{40}$).*";
    String regExpFor40Symbols = "^.{40}$";

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


}
