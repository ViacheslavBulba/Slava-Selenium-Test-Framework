package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ProductPage extends BasePage {

    @FindBy(css = ".crux-page-title.title-case")
    private WebElement header;

    public String getPageHeader() {
        return wait.until(ExpectedConditions.visibilityOf(header)).getText();
    }

}
