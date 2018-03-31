package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ConsumerReportsProductPage extends AbstractPage {

    @FindBy(className = "header-text")
    private WebElement header;

    public String getPageHeader() {
        return wait.until(ExpectedConditions.visibilityOf(header)).getText();
    }

}
