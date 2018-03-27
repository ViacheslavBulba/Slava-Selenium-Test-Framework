package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ConsumerReportsProductPage extends AbstractPage {

    @FindBy(className = "header-text")
    private WebElement header;

    public String getPageHeader() {
        WebDriverWait wait = new WebDriverWait(browser.getWebDriver(), 5);
        return wait.until(ExpectedConditions.visibilityOf(header)).getText();
    }

}
