package tests;

import org.testng.annotations.Test;
import pages.MenuPage;
import utils.BrowserTestSuite;
import utils.Logger;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertTrue;

public class UiTestsWithPageObjects extends BrowserTestSuite {

    @Test(description = "List of products is displayed on the menu page")
    public void checkListOfProducts() {
        MenuPage menu = new MenuPage();
        Logger.pass("Get text from all links");
        List<String> allLinksTexts = menu.getAllProductLinksTexts();
        Logger.pass("Check that specific product links are present on the A-Z page");
        menu.waitSeconds(2);
        menu.scrollDown(1000);
        List<String> productsToCheck = Arrays.asList("Cars", "Wagons");
        for (String product : productsToCheck) {
            assertTrue(allLinksTexts.contains(product), product + " product is missing on the page");
            Logger.pass("Check that [" + product + "] product is present on the page");
        }
    }
}
