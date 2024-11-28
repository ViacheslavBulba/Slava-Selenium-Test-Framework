package tests;

import org.testng.annotations.Test;
import pages.BasePage;
import utils.BrowserTestSuite;

import static org.testng.Assert.assertTrue;

public class TargetExampleWithoutPageObject extends BrowserTestSuite {

    @Test(description = "Search on Target.com")
    public void searchOnTarget() {
        String searchInput = "//*[@id='search']";
        String productCard = "//*[contains(@class,'StyledTitleLink') or @data-test=\"product-title\"]";
        BasePage page = new BasePage();
        page.openPage("https://www.target.com/");
        page.enterTextAndPressEnter(searchInput, "apple watch", "search input");
        page.waitSeconds(5);
        page.scrollDownWithPause(1000, 6);
        assertTrue(page.getNumberOfElements(productCard) > 20, "there should be more than 20 results");
        page.assertEachElementContainsText(productCard, "apple", "product card");
    }

    @Test(description = "Upper menu on Target.com")
    public void upperMenuOnTarget() {
        BasePage page = new BasePage();
        page.openPage("https://www.target.com/");
        page.clickOnText("Pickup & delivery");
        page.waitSeconds(1);
        page.clickOnText("Shop Order Pickup");
        page.assertTextIsPresent("Drive Up in the app");
        page.assertTextIsPresent("Grocery");
        page.assertTextIsMissing("Error");
        page.assertPageUrl("/order-pickup/");
    }
}
