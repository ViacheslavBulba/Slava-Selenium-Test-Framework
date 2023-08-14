package tests;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.BrowserHolder;
import utils.BrowserTestSuite;

import static org.hamcrest.Matchers.hasKey;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Random;

public class CombinedUiAndApiExample2 {//extends BrowserTestSuite {

//    @BeforeMethod
//    public void beforeEach() {
//        BrowserHolder.openUrl("https://lendage-interview-service.qa.lndgcp.com/user-fe.html");
//    }


    @Test
    public void test3Post(ITestContext context) {
        RestAssured.baseURI = "https://demoqa.com/BookStore/v1/Books";
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");

        JSONObject requestParams = new JSONObject();
        requestParams.put("userId", "TQ123");
        requestParams.put("isbn", "9781449325862");


        request.body(requestParams.toJSONString());
        Response response = request.post("/BookStoreV1BooksPost");

        System.out.println("The status received: " + response.statusLine());
    }

    @Test
    public void UserRegistrationSuccessful() {
        RestAssured.baseURI = "https://demoqa.com/Account/v1";
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        JSONObject requestParams = new JSONObject();

        Random random = new Random();
        String username = "Testing_" + random.nextInt(10000);
        String password = "Testing1!";
        requestParams.put("userName", username);
        requestParams.put("password", password);
        request.body(requestParams.toJSONString());

        Response response = request.post("/User");

        ResponseBody body = response.getBody();
        System.out.println(response.getStatusLine());
        System.out.println(body.asString());

        Headers allHeaders = response.headers();
        System.out.println("=== Headers (if needed) ===");
        for(Header header : allHeaders) {
            System.out.println(header.getName() + " : " + header.getValue());
        }

        assertEquals(response.header("Content-Type"), "application/json; charset=utf-8", "Unexpected Content-Type header");

        assertEquals(response.getStatusCode(), 201, "Unexpected response code for create account post request");
        assertEquals(response.getStatusLine(), "HTTP/1.1 201 Created", "Unexpected status line for create account post request");

        response.then().body("", hasKey("userID"));
        response.then().body("", hasKey("username"));
        response.then().body("", hasKey("books"));
        assertEquals(response.jsonPath().get("username"), username);
        assertTrue(response.jsonPath().getString("userID").length() > 30, "userID should have length > 30");

    }
}
