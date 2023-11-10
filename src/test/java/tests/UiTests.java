package tests;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.MenuPage;
import pages.ProductPage;
import utils.BrowserHolder;
import utils.BrowserTestSuite;
import utils.Logger;

import java.util.Arrays;
import java.util.List;

public class UiTests extends BrowserTestSuite {

//    @BeforeMethod // UNCOMMENT THIS METHOD IF YOU NEED TO OPEN DIFFERENT START URLS FROM DIFFERENT CLASSES WITH TESTS
//    public void beforeEach() {
//        openUrl("https://www.consumerreports.org/cro/a-to-z-index/products/index.htm");
//    }

    @Test(description = "List of products is displayed on the menu page")
    public void checkListOfProducts() {
        MenuPage menu = new MenuPage();
        Logger.pass("Get text from all links");
        List<String> allLinksTexts = menu.getAllProductLinksTexts();
        Logger.pass("Check that specific product links are present on the A-Z page");
        List<String> productsToCheck = Arrays.asList("Regular Yogurts", "Compact Washers", "Sunscreens");
        for (String product : productsToCheck) {
            assertTrue(allLinksTexts.contains(product), product + " product is missing on the page");
            Logger.pass("Check that [" + product + "] product is present on the page");
        }
    }

    @DataProvider(parallel = true)
    private Object[][] productsToTest() {
        return new Object[][]{
                {"Batteries"},
                {"Backpack Carriers"}
        };
    }

    @Test(description = "Click on product link", dataProvider = "productsToTest")
    public void clickOnProductLink(String linkTitle) {
        MenuPage menu = new MenuPage();
        menu.sleep(2); // sleep is added just for better visibility of what is going on in the browser, use WebDriverWait in real tests
        ProductPage product = menu.clickOnProductLink(linkTitle);
        product.sleep(2); // sleep is added just for better visibility of what is going on in the browser, use WebDriverWait in real tests
        Logger.pass("Check that the header of the opened page contains: " + linkTitle);
        assertTrue(product.getPageHeader().toLowerCase().contains(linkTitle.toLowerCase()), "Header on the top of the page should be the same as the link text");
    }
}
