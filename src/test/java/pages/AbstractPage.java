package pages;

import utils.PageFactoryLayer;

public class AbstractPage extends PageFactoryLayer {



  /*@FindBy(css = ".header-menu-crumbs")
  private WebElement headerMenuCrumbs;

  public String getHeaderMenuCrumbs(){
    WebDriverWait wait = new WebDriverWait(browser.getWebDriver(),5);
    wait.until(ExpectedConditions.visibilityOf(headerMenuCrumbs));
    ReportHolder.getReport().log(Status.PASS,"Getting header menu crumbs");
    ReportHolder.getReport().log(Status.PASS,"Header menu crumbs are: " + headerMenuCrumbs.getText().replace("\n", " > "));
    return headerMenuCrumbs.getText().replace("\n", " > ");
  }

  public boolean isElementDisplayed(WebElement element) {
    try {
      return element.isDisplayed();
    } catch (NoSuchElementException e) {
      return false;
    }
  }*/

}
