package tests;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.when;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import java.util.List;

public class ApiTests {

    @Test(description = "KuCoin public api test")
    public void kuCoinApiTest() {

        baseURI = "https://api.kucoin.com";

        when().
            get("/api/v1/market/stats?symbol=LTC-USDT").
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

    @Test
    public void getListOfAllBooksIds() {
        String response = get("https://demoqa.com/BookStore/v1/Books").asString();
        List<String> allBooksIds = from(response).getList("books.isbn");
        System.out.println(allBooksIds);
        assertTrue(allBooksIds.size() > 0, "empty books list");
    }
}