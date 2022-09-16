package tests;

import static io.restassured.http.ContentType.JSON;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.ITestContext;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ApiDevicesTests {

    private final String baseApiUri = "https://my.example.com";
    private final String otpEndpointPath = "/get/otp";
    private final String authEndpointPath = "/post/authenticate";
    private final String initialBasicAuthToken = "Basic pre-defined-token-1";
    private final String email = "test@test.com";
    private final String password = "Test1$";
    private RequestSpecification reqSpecSharedHeaders;

    private void assertResponseCodeAndTime(Response response, int code, long time) {
        assertEquals(response.getStatusCode(), code, "response code is not " + code);
        assertTrue(response.getTime() < time, "response took longer than " + time + " milliseconds");
    }

    private void assertFiledIsPresentInResponseAndNotEmpty(String value, String key) {
        assertNotNull(value, key + " is missing in response");
        assertTrue(value.length() > 0, key + " in response is empty");
    }

    private void getOtpToken(ITestContext context) {
        RequestSpecification otpRequest = RestAssured.given().spec(reqSpecSharedHeaders);
        otpRequest.header("Authorization", initialBasicAuthToken);
        Response otpResponse = otpRequest.get(otpEndpointPath);
        System.out.println(otpResponse.asString());
        assertResponseCodeAndTime(otpResponse, 200, 2000L);
        String otp = otpResponse.jsonPath().getString("otp");
        assertFiledIsPresentInResponseAndNotEmpty(otp, "otp");
        context.setAttribute("otp", otp);
    }

    private void getAuthToken(ITestContext context) {
        RequestSpecification authRequest = RestAssured.given().spec(reqSpecSharedHeaders);
        authRequest.header("Authorization", initialBasicAuthToken);
        authRequest.header("X-CareSmart-Token", context.getAttribute("otp"));
        Map<String, Object> body = new HashMap<>(); // JSONObject jsonBody = new JSONObject();
        body.put("username", email);
        body.put("password", password);
        body.put("email", email);
        body.put("mode", "password");
        authRequest.body(body);
        Response authResponse = authRequest.post(authEndpointPath);
        System.out.println(authResponse.asString());
        assertResponseCodeAndTime(authResponse, 200, 2000L);
        String accessToken = authResponse.jsonPath().getString("access_token");
        assertFiledIsPresentInResponseAndNotEmpty(accessToken, "access_token");
        context.setAttribute("accessToken", "Bearer " + accessToken);
    }

    private void getAllDevicesAndFirstDeviceId(ITestContext context) {
        RequestSpecification devicesRequest = RestAssured.given().spec(reqSpecSharedHeaders);
        devicesRequest.header("Authorization", context.getAttribute("accessToken"));
        Response devicesResponse = devicesRequest.get("/devices");
        System.out.println(devicesResponse.asString());
        assertResponseCodeAndTime(devicesResponse, 200, 2000L);
        List<Map<String, Object>> devices = devicesResponse.as(new TypeRef<List<Map<String, Object>>>() {
        });
        assertTrue(devices.size() > 0, "no devices in response");
        assertNotNull(devices.get(0).get("id"), "there is no id field in response");
        String deviceId = devices.get(0).get("id").toString();
        System.out.println("deviceId = " + deviceId);
        assertFiledIsPresentInResponseAndNotEmpty(deviceId, "id");
        context.setAttribute("deviceId", deviceId);
    }

    @BeforeSuite
    private void setUp(ITestContext context) {
        RestAssured.baseURI = baseApiUri;
        String transactionId = String.valueOf(UUID.randomUUID());
        reqSpecSharedHeaders = new RequestSpecBuilder().
            setContentType(JSON).
            addHeader("X-Transaction-Id", transactionId).
            build();
        getOtpToken(context);
        getAuthToken(context);
        getAllDevicesAndFirstDeviceId(context);
    }

    @Test
    private void getDeviceByIdTest(ITestContext context) {
        RequestSpecification getDeviceByIdRequest = RestAssured.given().spec(reqSpecSharedHeaders);
        getDeviceByIdRequest.header("Authorization", context.getAttribute("accessToken"));
        getDeviceByIdRequest.header("X-Delegate-Id", "null");
        Response getDeviceResponse = getDeviceByIdRequest.get("/devices/" + context.getAttribute("deviceId"));
        System.out.println(getDeviceResponse.asString());
        assertResponseCodeAndTime(getDeviceResponse, 200, 2000L);
        assertEquals(getDeviceResponse.jsonPath().getString("id"), context.getAttribute("deviceId"), "unexpected device id");
        assertEquals(getDeviceResponse.jsonPath().getString("kind"), "ts.device.mpers.watch", "unexpected device kind");
    }
}
