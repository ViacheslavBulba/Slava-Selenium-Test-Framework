package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.Logger;

import java.util.List;
import java.util.stream.Collectors;

public class ConsumerReportsAZProductsPage extends AbstractPage {

    @FindBy(css = ".products-a-z__results__item")
    private List<WebElement> productLinks;

    public List<String> getAllLinks() {
        return productLinks.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public ConsumerReportsProductPage clickOnProductLink(String link) {
        Logger.info("Click on product link: " + link);
        By locator = By.xpath(String.format(".//*[@class='products-a-z__results']//*[contains(text(), '%s')]", link));
        WebElement element = browser.getWebDriver().findElement(locator);
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
        return new ConsumerReportsProductPage();
    }

}
