package pages;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.PageFactoryLayer;

public class AbstractPage extends PageFactoryLayer {

    WebDriverWait wait = new WebDriverWait(browser.getWebDriver(), 5);

    public boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

}
