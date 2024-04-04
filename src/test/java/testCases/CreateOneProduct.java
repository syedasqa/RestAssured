package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import java.io.File;

public class CreateOneProduct {

	String baseURI;
	SoftAssert softAssert;
	String firstProductId;
	String createPayloadFilePath;

	public CreateOneProduct() {
		baseURI = "https://techfios.com/api-prod/api/product";
		softAssert = new SoftAssert();
		createPayloadFilePath = "src/main/java/data/CreatePayload.json";
	}

	@Test(priority = 1)
	public void createOneProduct() {

		Response response = 
				given()
				.baseUri(baseURI)
				.header("Content-Type", "application/json; charset=UTF-8")
				.header("Authorization", "Bearer dskjfhsdkjfhds66#$%dsfhdhfh").body(new File(createPayloadFilePath)).when()
				.post("/create.php").then().extract().response();

		String responseBody = response.getBody().asString();
		System.out.println("Response Body:" + responseBody);
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
		firstProductId = jp.get("records[0].id");
		System.out.println("readAllProducts -> First Product Id :" + firstProductId);
	}

	@Test(priority = 3)
	public void readOneProduct() {

		Response response = given().baseUri(baseURI).header("Content-Type", "application/json")
				.header("Authorization", "Bearer dskjfhsdkjfhds66#$%dsfhdhfh").queryParam("id", firstProductId).when()
				.get("/read_one.php").then().extract().response();

		JsonPath jp2 = new JsonPath(new File(createPayloadFilePath));
		String expectedProductName = jp2.get("name");
		String expectedProductDescription = jp2.get("description");
		String expectedProductPrice = jp2.get("price");

		String responseBody = response.getBody().asString();
		System.out.println("Response Body:" + responseBody);

		JsonPath jp = new JsonPath(responseBody);
		String actualProductName = jp.get("name");
		System.out.println("Actual Product Name: " + actualProductName);
		System.out.println("Expected Product Name: " + expectedProductName);
		softAssert.assertEquals(actualProductName, expectedProductName, "Products Names are not matching!");

		String actualProductDescription = jp.get("description");
		System.out.println("Actual Product Description: " + actualProductDescription);
		System.out.println("Expected Product Description: " + expectedProductDescription);
		softAssert.assertEquals(actualProductDescription, expectedProductDescription,
				"Products Descriptions are not matching");

		String ActualProductPrice = jp.get("price");
		System.out.println("Actual Product Price: " + ActualProductPrice);
		System.out.println("Expected Product Price: " + expectedProductPrice);
		softAssert.assertEquals(ActualProductPrice, expectedProductPrice, "Products Prices are not matching!");

		softAssert.assertAll();
	}
}
