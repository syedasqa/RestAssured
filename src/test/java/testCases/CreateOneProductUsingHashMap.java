package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CreateOneProductUsingHashMap {

	String baseURI;
	SoftAssert softAssert;
	String firstProductId;
	String createPayloadFilePath;
	HashMap<String, String> createPayload;
	public CreateOneProductUsingHashMap() {
		baseURI = "https://techfios.com/api-prod/api/product";
		softAssert = new SoftAssert();
		createPayloadFilePath = "src/main/java/data/CreatePayload.json";
		createPayload = new HashMap<String, String>();
	}
	
	public Map<String, String> createPayloadMap() {
		createPayload.put("name", "Md's Amazing pillow 2.0");
		createPayload.put("price", "299");
		createPayload.put("description", "The best pillow for amazing QA peoples");
		createPayload.put("category_id", "2");
		createPayload.put("category_name", "Electronics");
		
		return createPayload;
		
	}
	@Test(priority = 1)
	public void createOneProducts() {

		Response response = given().baseUri(baseURI).header("Content-Type", "application/json; charset=UTF-8")
				.header("Authorization", "Bearer dskjfhsdkjfhds66#$%dsfhdhfh").body(createPayloadMap()).when()
				.post("/create.php").then().extract().response();
		
		int statusCode = response.getStatusCode();
		System.out.println("Status Code:" + statusCode);
		softAssert.assertEquals(statusCode, 201, "Status codes are not matching!");
		
		long responseTime = response.getTime();
		System.out.println("ResponseTime:" +responseTime);
		if(responseTime <=2000) {
			
			System.out.println("Response time is within range");
		}else {
			System.out.println("Response time is out of range!");
		}
		

		String responseBody = response.getBody().asString();
		System.out.println("Response Body:" + responseBody);

		JsonPath jp = new JsonPath(responseBody);
		firstProductId = jp.get("records[0].id");
		System.out.println("First Product Id :" + firstProductId);
	}

	@Test(priority = 2)
	public void readAllProducts() {

		Response response = given().baseUri("https://techfios.com/api-prod/api/product")
				.header("Content-Type", "application/json; charset=UTF-8").auth().preemptive()
				.basic("demo@techfios.com", "abc123").
				when()
				.get("/read.php").then().extract().response();

		String responseBody = response.getBody().asString();
		System.out.println("Response Body:" + responseBody);

		JsonPath jp = new JsonPath(responseBody);
		String firstProductId = jp.get("records[0].id");
		System.out.println("First Product Id :" + firstProductId);
	}

	//@Test(priority = 3)
	public void readOneProducts() {

		Response response = given().baseUri(baseURI).header("Content-Type", "application/json")
				.header("Authorization", "Bearer dskjfhsdkjfhds66#$%dsfhdhfh").queryParam("id", firstProductId).when()
				.get("/read_one.php").then().extract().response();

		//JsonPath jp2 = new JsonPath(new File(createPayloadFilePath));
		String expectedProductName = createPayloadMap().get("name");
		String expectedProductDescription = createPayloadMap().get("description");
		String expectedProductPrice = createPayloadMap().get("price");

		String responseBody = response.getBody().asString();
		System.out.println("Response Body:" + responseBody);

		JsonPath jp = new JsonPath(responseBody);
		String actualProductName = jp.get("name");
		System.out.println("Actual Product Name :" + actualProductName);
		softAssert.assertEquals(actualProductName, expectedProductName, "Products Names are not matching!");

		String actualProductDescription = jp.get("description");
		System.out.println("Actual Product Description :" + actualProductDescription);
		softAssert.assertEquals(actualProductDescription, expectedProductDescription,
				"Products Descriptions are not matching");

		String ActualProductPrice = jp.get("Price");
		System.out.println("Actual Product Price :" + ActualProductPrice);
		softAssert.assertEquals(ActualProductPrice, expectedProductPrice, "Products Prices are not matching!");

		softAssert.assertAll();
	}

}
