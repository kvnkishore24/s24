package api.login;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.annotations.Test;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

import utils.ConfiguratorSupport;

public class IntrinoLogin {
	public static ConfiguratorSupport config;
	public static Response response;
	public static String jsonAsString;
	public static JsonPath jp;
	Logger log = Logger.getLogger(IntrinoLogin.class.getName());

	@Test(priority = 1, groups = "LoginAccessAPI_g")
	public void loginWithValidDetails() {

		log = Logger.getLogger(Thread.currentThread().getStackTrace()[1].getMethodName());
		String method_name = Thread.currentThread().getStackTrace()[1].getMethodName();
		PropertyConfigurator.configure("Log4j.properties");
		log.info("TESTCASE::______________" + method_name);
		try {

			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
				}
			} };

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			//System.setProperty("https.protocols", "SSLv3");
			// System.setProperty("javax.net.debug", "all");
			// System.setProperty("https.protocols", "TLSv1.2");
			// config(RestAssured.config().sslConfig(sslConfig().relaxedHTTPSValidation("TLSv1.2")))
			response = given().relaxedHTTPSValidation("TLSv1.2").log().all().auth().preemptive().basic("XXXXXXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXXXXXX").when()
					.get("https://api.intrinio.com/prices?identifier=IBN&start_date=2017-02-22&end_date=2017-03-01").then().body("data", notNullValue()).extract().response();
			// .body("data.items.token", "")
			// .body("isEmpty()", Matchers.is(true))
			// .body(error.message, equalToIgnoringCase(expectedString)"")
			// User Has Been Suspended
			System.err.println(response.asString());
			String json = response.asString();
			JsonPath jp = new JsonPath(json);

			
			
			
			
			
			
			// System.out.println("----\n" + jp.getInt("apiVersion") +
			// "\n------");
			// System.err.println("----\n" + jp.getString("responseId") +
			// "\n------");
			// System.out.println("----\n" +
			// jp.getString("data.items.token").substring(1,
			// jp.getString("data.items.token").length() - 1) + "\n------");

		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.err.println(e.getLocalizedMessage());
		}

	}
}
