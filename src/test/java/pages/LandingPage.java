package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LandingPage extends BasePage {

    @FindBy(css = "#name-post")
    private WebElement nameInput;

    @FindBy(css = "#email-post")
    private WebElement emailInput;

    @FindBy(xpath = "//button[contains(text(),'Create')]")
    private WebElement createButton;

    @FindBy(xpath = "//textarea[1]")
    private WebElement textArea;

    @FindBy(css = "#name-patch")
    private WebElement updateNameInput;

    @FindBy(css = "#email-patch")
    private WebElement updateEmailInput;

    @FindBy(css = "#id-patch")
    private WebElement idInput;

    @FindBy(xpath = "//button[contains(text(),'Update')]")
    private WebElement updateButton;

    public void enterName(String name) {
        nameInput.click();
        nameInput.clear();
        nameInput.sendKeys(name);
    }

    public void enterEmail(String email) {
        emailInput.click();
        emailInput.clear();
        emailInput.sendKeys(email);
    }

    public void clickOnCreateButton() {
        createButton.click();
    }

    public String getTextFromTextArea() {
        wait.until(ExpectedConditions.attributeContains(textArea, "value", "{"));
        return textArea.getAttribute("value");
    }

    public void enterUpdatedName(String name) {
        updateNameInput.click();
        updateNameInput.clear();
        updateNameInput.sendKeys(name);
    }

    public void enterUpdatedEmail(String email) {
        updateEmailInput.click();
        updateEmailInput.clear();
        updateEmailInput.sendKeys(email);
    }

    public void clickOnUpdateButton() {
        updateButton.click();
    }

    public void enterId(String id) {
        idInput.click();
        idInput.clear();
        idInput.sendKeys(id);
    }

}
