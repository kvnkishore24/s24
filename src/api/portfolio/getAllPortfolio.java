package api.portfolio;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.testng.annotations.Test;

import com.jayway.restassured.module.jsv.JsonSchemaValidator;
import com.jayway.restassured.parsing.Parser;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

import base.BaseTest;
import utils.ReusableMethods;

public class getAllPortfolio extends ReusableMethods {

	public static Response response;
	public static String jsonAsString;
	public static JsonPath jp;
	
	Logger log = Logger.getLogger(getAllPortfolio.class.getName());

	@Test(priority = 1, enabled = true,dependsOnGroups="LoginAccessAPI_g")
	public void getPortfolios() {
		try {
			response =	given().
								headers("X-Auth-Token", BaseTest.getX_Auth_Token()).
						when().
								get("/portfolio/get/453").
						then().log().body().
								parser("text/html", Parser.JSON).
								statusCode(200).
								and().
								body("error.any { it.key == 'message' }", is(false)).
								body("error.any { it.key == 'code' }", is(false)).
								body("any { it.key == 'apiVersion' }", is(true)).
								body("apiVersion", equalTo(1)).
								body("any { it.key == 'responseId' }", is(true)).
								body("responseId", notNullValue()).
								body(matchesJsonSchemaInClasspath("success.json")).
								body("data.any { it.key == 'items' }", is(true)).
					extract().response();
		} finally {
			JsonSchemaValidator.reset();
		}
	}
}
