package tests;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.MenuPage;
import pages.ProductPage;
import utils.BrowserTestSuite;
import utils.Logger;

public class Tests extends BrowserTestSuite {

    @DataProvider(parallel = true)
    private Object[][] productsToTest() {
        return new Object[][]{
            {"Batteries"},
            {"Backpack Carriers"}
        };
    }

    @Test(description = "Click on product link",
        dataProvider = "productsToTest")
    public void clickOnProductLink(String linkTitle) {
        MenuPage menu = new MenuPage();
        menu.sleep(2); // sleep is added just for better visibility of what is going on in the browser, use WebDriverWait in real tests
        ProductPage product = menu.clickOnProductLink(linkTitle);
        product.sleep(2); // sleep is added just for better visibility of what is going on in the browser, use WebDriverWait in real tests
        Logger.pass("Check that the header of the opened page contains: " + linkTitle);
        assertTrue(product.getPageHeader().toLowerCase().contains(linkTitle.toLowerCase()));
    }
}
