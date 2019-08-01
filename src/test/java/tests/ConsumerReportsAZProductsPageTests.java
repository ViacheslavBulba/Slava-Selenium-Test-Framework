package tests;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.ConsumerReportsAZProductsPage;
import pages.ConsumerReportsProductPage;
import utils.BrowserTestSuite;
import utils.Logger;

import java.util.List;

public class ConsumerReportsAZProductsPageTests extends BrowserTestSuite {

    @DataProvider(parallel = true)
    private Object[][] productsToTest() {
        return new Object[][]{
            {"Air Conditioners"},
            {"Airline Travel"},
            {"Bacon"},
            {"Beer"},
            {"Batteries"},
            {"Cameras"},
            {"Test To Fail"},
            {"Car seats"}
        };
    }

    @Test(description = "Check that links on the A-Z page open appropriate pages",
        dataProvider = "productsToTest")
    public void checkAllLinksWork(String linkTitle) throws Exception {
        ConsumerReportsAZProductsPage azPage = new ConsumerReportsAZProductsPage();
        ConsumerReportsProductPage productPage = azPage.clickOnProductLink(linkTitle);
        Logger.pass("Check that the header of the opened page contains: " + linkTitle);
        assertTrue(productPage.getPageHeader().toLowerCase().contains(linkTitle.toLowerCase()));
    }

    @Test(description = "There are more than 100 A-Z links on the page")
    public void thereAreMoreThan100AtoZlinksOnThePage() throws Exception {
        ConsumerReportsAZProductsPage azPage = new ConsumerReportsAZProductsPage();
        List<String> productCategoryOnAtoZpageLinks = azPage.getAllLinks();
        Logger.pass("Check that there are more than 100 links on the page");
        Logger.info("There are links on the page: " + productCategoryOnAtoZpageLinks.size());
        assertFalse(productCategoryOnAtoZpageLinks.size() < 100, String
            .format("There are less than 100 links on the page: %s", productCategoryOnAtoZpageLinks.size()));
    }

}
