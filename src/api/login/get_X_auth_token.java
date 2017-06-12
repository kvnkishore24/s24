package api.login;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.restassured.parsing.Parser;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

import base.BaseTest;


public class get_X_auth_token extends BaseTest {

	public static Response response;
	public static String jsonAsString;
	public static JsonPath jp;
	public static String X_Auth_Token;
	Logger log = Logger.getLogger(get_X_auth_token.class.getName());

	@Test(priority = 1)
	public void loginWithInvalidDetails() {
		try {
			response = given().log().all().auth().preemptive().basic(config.getProperty("inValidEmail"), config.getProperty("inValidPassword")).when().post("/user/login").then().parser("text/html", Parser.JSON)
					.log().body().statusCode(200).body(matchesJsonSchemaInClasspath("error.json")).body("any { it.key == 'apiVersion' }", is(true)).body("any { it.key == 'responseId' }", is(true)).body("apiVersion", notNullValue())
					.body("responseId", notNullValue()).body("data.any { it.key == 'items' }", is(false)).body("error.any { it.key == 'message' }", is(true)).body("error.any { it.key == 'code' }", is(true)).body("error.code", equalTo(400))
					.extract().response();
		} catch (Exception e) {
			Assert.assertTrue(false, e.getMessage());
		}
	}

	@Test(priority = 2, groups = "LoginAccessAPI_g")
	public void loginWithValidDetails() {
		response = given().log().all().auth().preemptive().basic(config.getProperty("validEmail"), config.getProperty("validPassword")).when().post("/user/login").then().parser("text/html", Parser.JSON).log()
				.body().statusCode(Integer.parseInt(config.getProperty("exptectedStatusCode"))).body("any { it.key == 'apiVersion' }", is(true)).body("any { it.key == 'responseId' }", is(true)).body("apiVersion", equalTo(1))
				.body("responseId", notNullValue())
				// .body("data.items.token", "")
				// .body("isEmpty()", Matchers.is(true))
				// .body(error.message, equalToIgnoringCase(expectedString)"")
				// User Has Been Suspended
				.body("error.any { it.key == 'message' }", is(false)).body("error.any { it.key == 'code' }", is(false)).extract().response();

		String json = response.asString();
		JsonPath jp = new JsonPath(json);
		BaseTest.setX_Auth_Token(jp.getString("data.items.token").substring(1, jp.getString("data.items.token").length() - 1));
		System.err.println("USER:: "+config.getProperty("validEmail")+"\nPWD:: "+config.getProperty("validPassword"));

		// System.out.println("----\n" + jp.getInt("apiVersion") + "\n------");
		// System.err.println("----\n" + jp.getString("responseId") +
		// "\n------");
		// System.out.println("----\n" +
		// jp.getString("data.items.token").substring(1,
		// jp.getString("data.items.token").length() - 1) + "\n------");
	}
}
