package tests;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.ConsumerReportsAZProductsPage;
import pages.ConsumerReportsProductPage;
import utils.BrowserTestSuite;
import utils.FileSystem;
import utils.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConsumerReportsAZProductsPageTests extends BrowserTestSuite {

    private static final String EMPTY_LIST_MESSAGE = "EMPTY LIST OF LINKS";

    @DataProvider(parallel = true)
    public Iterator<Object[]> allLinkTitlesOnAzPage() throws IOException {
        Document doc = Jsoup.connect(FileSystem.getPropertyFromFile("url")).get();
        Elements links = doc.select(".products-a-z__results__item");
        if (links.size() < 1) {
            ArrayList<Object[]> emptyList = new ArrayList<>();
            emptyList.add(new Object[]{EMPTY_LIST_MESSAGE});
            return emptyList.iterator();
        }
        return links.subList(0, 8).stream().map(l -> new Object[]{l.text()}).iterator();
    }

    @Test(description = "Check that links on the A-Z page open appropriate pages",
        dataProvider = "allLinkTitlesOnAzPage")
    public void checkAllLinksWork(String linkTitle) throws Exception {
        String linkSingular = linkTitle.endsWith("s") ? linkTitle.substring(0, linkTitle.length() - 1) : linkTitle;
        ConsumerReportsAZProductsPage azPage = new ConsumerReportsAZProductsPage();
        assertFalse(EMPTY_LIST_MESSAGE.equals(linkTitle), EMPTY_LIST_MESSAGE);
        ConsumerReportsProductPage productPage = azPage.clickLink(linkTitle);
        if (linkSingular.equals("Home window")) {
            linkSingular = "Replacement Windows";
        }
        Logger.pass("Check that the header of the opened page contains: " + linkSingular);
        assertTrue(productPage.getPageHeader().toLowerCase().contains(linkSingular.toLowerCase()));
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
