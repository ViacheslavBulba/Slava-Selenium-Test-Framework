package tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;
import static org.testng.Assert.assertEquals;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.ITestContext;
import org.testng.annotations.Test;
import pages.LandingPage;
import utils.BrowserTestSuite;
import utils.Logger;

public class LandingPageTests extends BrowserTestSuite {

    // https://lendage-interview-service.qa.lndgcp.com/user-fe.html

    // https://lendage-interview-service.qa.lndgcp.com/index-sdet.html

    @Test(description = "create user")
    public void createUserAndCheckDataIsReturnedByGetRequest(ITestContext context) {
        LandingPage page = new LandingPage();
        String expectedName = generateRandomString(15);
        String expectedEmail = generateRandomString(15) + "@" + generateRandomString(7) + "." + generateRandomString(3);
        Logger.info("Enter name and email: " + expectedName + " / " + expectedEmail);
        page.enterName(expectedName);
        page.enterEmail(expectedEmail);
        page.clickOnCreateButton();
        String createUserResponse = page.getTextFromTextArea();
        JSONObject jsonObject = new JSONObject(createUserResponse);
        String id = jsonObject.get("id").toString();
        System.out.println(id);
        context.setAttribute("id", id);
        Response response = given().filter(new AllureRestAssured()).get("https://lendage-interview-service.qa.lndgcp.com/sdet/users/" + id);
        response.then().body("", hasKey("name"));
        assertEquals(response.jsonPath().getString("name"), expectedName);
        response.then().body("", hasKey("email"));
        assertEquals(response.jsonPath().getString("email"), expectedEmail);
    }

    @Test(description = "update user", dependsOnMethods = {"createUserAndCheckDataIsReturnedByGetRequest"})
    public void updateUserAndCheckDataIsReturnedByGetRequest(ITestContext context) {
        LandingPage page = new LandingPage();
        String id = context.getAttribute("id").toString();
        page.enterId(id);
        String expectedName = generateRandomString(15);
        String expectedEmail = generateRandomString(15) + "@" + generateRandomString(7) + "." + generateRandomString(3);
        page.enterUpdatedName(expectedName);
        page.enterUpdatedEmail(expectedEmail);
        page.clickOnUpdateButton();
        page.getTextFromTextArea(); // wait for response
        Response response = given().filter(new AllureRestAssured()).get("https://lendage-interview-service.qa.lndgcp.com/sdet/users/" + id);
        response.then().body("", hasKey("name"));
        assertEquals(response.jsonPath().getString("name"), expectedName);
        response.then().body("", hasKey("email"));
        assertEquals(response.jsonPath().getString("email"), expectedEmail);
    }
}
