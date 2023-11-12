package tests;

import org.testng.annotations.Test;
import pages.BasePage;
import utils.BrowserTestSuite;

public class TargetExampleWithoutPageObject extends BrowserTestSuite {

    @Test(description = "Search on Target.com")
    public void searchOnTarget() {
        String searchInput = "//*[@id='search']";
        String productCard = "//*[contains(@class,'StyledTitleLink')]";
        BasePage page = new BasePage();
        page.openPage("https://www.target.com/");
        page.enterTextAndPressEnter(searchInput, "apple watch", "search input");
        page.waitSeconds(5);
        page.scrollDownWithPause(1000, 6);
        page.assertNumberOfElements(productCard, 24, "product card");
        page.assertEachElementContainsText(productCard, "apple watch", "product card");
    }
}
