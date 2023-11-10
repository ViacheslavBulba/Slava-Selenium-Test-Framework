package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.testng.ITestContext;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;
import static utils.FileSystem.clearFolder;

public class ApiTests {

    private String getDateAndTime() {
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss_SSS");
        return formatForDateNow.format(dateNow);
    }

    @BeforeSuite
    private void setUp(ITestContext context) {
        clearFolder("allure-results");
        RestAssured.baseURI = "https://reqres.in";
    }

    private void assertResponseCodeAndTime(Response response, int code, long time) {
        assertEquals(response.getStatusCode(), code, "response code is not " + code);
        assertTrue(response.getTime() < time, "response took longer than " + time + " milliseconds");
    }

    @Test(description = "GET - KuCoin public api test")
    public void kuCoinApiTest() {
        given().filter(new AllureRestAssured()).
                when().
                get("https://api.kucoin.com/api/v1/market/stats?symbol=LTC-USDT").
                then().
                statusCode(200).
                log().body().
                time(lessThan(4000L)).
                contentType(JSON).
                body("", hasKey("code")).
                body("code", not(empty())).
                body("code", equalTo("200000")).
                body("data", hasKey("time")).
                body("data.time", not(empty()));
    }

    @Test(description = "POST - createUserViaPost")
    public void createUserViaPost(ITestContext context) {
        String name = String.valueOf(UUID.randomUUID());
        String job = String.valueOf(UUID.randomUUID());
        context.setAttribute("name", name);
        context.setAttribute("job", job);
        JSONObject body = new JSONObject();
        body.put("name", name);
        body.put("job", job);
        System.out.println(body);
        RequestSpecification request = RestAssured.given().filter(new AllureRestAssured());
        request.header("Content-Type", "application/json"); // Content-Type: application/json; charset=utf-8
        request.body(body);
        Response response = request.post("/api/users");
        response.prettyPrint();
        assertResponseCodeAndTime(response, 201, 5000L); // 201 Created
        response.then().body("", hasKey("id"));
        String id = response.jsonPath().getString("id");
        assertNotNull(id, "id - is missing in response");
        assertTrue(id.length() > 0, "id - is empty in response");
        System.out.println("id = " + id);
        context.setAttribute("id", id);
    }

    @Test(description = "PATCH - updateUserViaPatch", dependsOnMethods = {"createUserViaPost"})
    public void updateUserViaPatch(ITestContext context) {
        String newName = getDateAndTime();
        JSONObject body = new JSONObject();
        body.put("name", newName);
        System.out.println(body);
        RequestSpecification request = RestAssured.given().filter(new AllureRestAssured());
        request.header("Content-Type", "application/json");
        request.body(body);
        Response response = request.patch("/api/users/2");
        response.prettyPrint();
        assertResponseCodeAndTime(response, 200, 4000L);
        response.then().body("", hasKey("updatedAt"));
        String updatedAt = response.jsonPath().getString("updatedAt");
        assertNotNull(updatedAt, "updatedAt - is missing in response");
        assertTrue(updatedAt.length() > 0, "updatedAt - is empty in response");
    }
}