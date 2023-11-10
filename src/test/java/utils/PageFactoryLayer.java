package utils;

import org.openqa.selenium.support.PageFactory;

public class PageFactoryLayer extends BrowserTestSuite {
  protected Browser browser;

  public PageFactoryLayer() {
    this.browser = BrowserHolder.getBrowser();
    PageFactory.initElements(browser.getWebDriver(),this);
  }

}
