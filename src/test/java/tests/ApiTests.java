package tests;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static utils.FileSystem.clearFolder;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.testng.ITestContext;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.List;
import java.util.UUID;

public class ApiTests {

    @BeforeSuite
    private void setUp(ITestContext context) {
        clearFolder("allure-results");
        RestAssured.baseURI = "https://reqres.in";
    }

    @Test(description = "GET - KuCoin public api test")
    public void kuCoinApiTest() {
        given().filter(new AllureRestAssured()).
        when().
            get("https://api.kucoin.com/api/v1/market/stats?symbol=LTC-USDT").
        then().
            statusCode(200).
            log().body().
            time(lessThan(2000L)).
            contentType(JSON).
            body("", hasKey("code")).
            body("code", not(empty())).
            body("code", equalTo("200000")).
            body("data", hasKey("time")).
            body("data.time", not(empty()));

//        given().
//            param("x", "y").
//            header("z", "w").
//        when().
//            get("/something").
//        then().
//            statusCode(200).
//            body("x.y", equalTo("z"));

//        String saveToVariable =
//            when().
//                get("/title").
//            then().extract().
//                path("_links.next.href");

//        // You could also decide to instead return the entire response if you need to extract multiple values from the response
//        Response response =
//            given().
//                param("param_name", "param_value").
//            when().
//                get("/title").
//            then().
//                contentType(JSON).
//                body("title", equalTo("My Title")).
//            extract().
//                response();
//        String nextTitleLink = response.path("_links.next.href");
//        String headerValue = response.header("headerName");

//        get("/lotto").then().body("lotto.winners.winnerId", hasItems(23, 54));

//         String json = get("/lotto").asString();
//
//         when().
//             get("/store").
//             then().
//             body("store.book.findAll { it.price < 10 }.title", hasItems("Sayings of the Century", "Moby Dick"));
//
//         JSON Schema validation is also supported if needed
//         get("/products").then().assertThat().body(matchesJsonSchemaInClasspath("products-schema.json"));
//         import static io.restassured.module.jsv.JsonSchemaValidator

//         Floating point numbers must be compared with a Java "float" primitive.
//         for example, {"price":12.12} should be compare with get("/price").then().assertThat().body("price", equalTo(12.12f)); and NOT equalTo(12.12)

//        List<Map<String, Object>> products = get("/products").as(new TypeRef<List<Map<String, Object>>>() {});
//        assertEquals(products.size(), 12, "size is not 12");
//        assertEquals(products.get(0).get("id"), 2, "first id is not 2");
    }

    @Test(description = "GET - getUsers")
    public void getUsers() {
        given().filter(new AllureRestAssured()).
        get("/api/users?page=2").
        then().
            statusCode(200).
            log().body().
            time(lessThan(2000L)).
            contentType(JSON).
            body("", hasKey("data")).
            body("data", not(empty())).
            //body("data", hasKey("first_name")). // cannot use hasKey on array
            body("data.first_name", hasItems("Michael", "Lindsay"));
    }

    private void assertResponseCodeAndTime(Response response, int code, long time) {
        assertEquals(response.getStatusCode(), code, "response code is not " + code);
        assertTrue(response.getTime() < time, "response took longer than " + time + " milliseconds");
    }

    private void assertFiledIsPresentInResponseAndNotEmpty(String fieldValue, String fieldDescriptionForErrorMessage) {
        assertNotNull(fieldValue, fieldDescriptionForErrorMessage + " - is missing in response");
        assertTrue(fieldValue.length() > 0, fieldDescriptionForErrorMessage + " - is empty in response");
    }

    @Test(description = "GET - getListOfAllNames")
    public void getListOfAllNames() {
        Response response = given().filter(new AllureRestAssured()).get("/api/users?page=2");
        System.out.println(response.asString());
        assertResponseCodeAndTime(response,200, 2000L);
        List<String> allNames = from(response.asString()).getList("data.first_name");
        System.out.println(allNames);
        assertTrue(allNames.size() > 0, "names list is empty");
    }

    @Test(description = "POST - createUserViaPost")
    public void createUserViaPost(ITestContext context) {
//        given().
//            body("{\"name\":\"test\",\"job\":\"test automation\"}").
//        when().
//            post("https://reqres.in/api/users").
//        then().
//            statusCode(201).
//            log().body().
//            body("", hasKey("id")).
//            body("id", not(empty()));
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
        String id = response.jsonPath().getString("id");
        assertNotNull(id, "id - is missing in response"); // or use assertFiledIsPresentInResponseAndNotEmpty(id, "id");
        assertTrue(id.length() > 0, "id - is empty in response");
        System.out.println("id = " + id);
        context.setAttribute("id", id);
        // also get user by id after post
    }

//    @Test(dependsOnMethods = {"createUserViaPost"})
//    public void getCreatedUser(ITestContext context) {
//        Response response = get("/api/users/" + context.getAttribute("id"));
//        System.out.println(response.asString());
//        assertResponseCodeAndTime(response,200, 2000L);
//    }

    @Test(description = "PATCH - updateUserViaPatch", dependsOnMethods = {"createUserViaPost"}) // (dependsOnMethods = {"postRequestExample"})
    public void updateUserViaPatch(ITestContext context) {
        String newName = "patched name";
        JSONObject body = new JSONObject();
        body.put("name", newName);
        System.out.println(body);
        RequestSpecification request = RestAssured.given().filter(new AllureRestAssured());
        request.header("Content-Type", "application/json");
        request.body(body);
        Response response = request.patch("/api/users/2"); // or take id from context
        response.prettyPrint();
        assertResponseCodeAndTime(response, 200, 2000L);
        //String name = response.jsonPath().getString("name");
        //assertFiledIsPresentInResponseAndNotEmpty(name, "name");
        String updatedAt = response.jsonPath().getString("updatedAt");
        assertFiledIsPresentInResponseAndNotEmpty(updatedAt, "updatedAt");
        //assertEquals(name, newName, "name was not updated in response");
        // also get user by id after patch
    }
}