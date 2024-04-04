package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

public class ReadOneProduct {

	String baseURI;
	SoftAssert softAssert;
	
	public ReadOneProduct() {
		baseURI = "https://techfios.com/api-prod/api/product";
		softAssert = new SoftAssert();
	}

	@Test
	public void readOneProducts() {

		Response response =

				given()
				.baseUri(baseURI)
				.header("Content-Type", "application/json")
						.header("Authorization", "Bearer dskjfhsdkjfhds66#$%dsfhdhfh")
						.queryParam("id", "7985").
						when()
						.get("/read_one.php").
						then()
						.extract().response();
		
		int statusCode = response.getStatusCode();
		System.out.println("Status Code:" + statusCode);
		//Assert.assertEquals(statusCode, 200);
		softAssert.assertEquals(statusCode, 200);
		long responseTime = response.getTime();
		System.out.println("ResponseTime:" + responseTime);
		if (responseTime <= 2000) {

			System.out.println("Response time is within range");
		} else {
			System.out.println("Response time is out of range!");
		}

		String responseHeaderContentType = response.getHeader("Content-Type");
		System.out.println("Response Header Content Type:" + responseHeaderContentType);
		//Assert.assertEquals(responseHeaderContentType, "application/json");
		softAssert.assertEquals(responseHeaderContentType, "application/json");

		String responseBody = response.getBody().asString();
		System.out.println("Response Body:" + responseBody);

		JsonPath jp = new JsonPath(responseBody);
		String productName = jp.get("name");
		System.out.println("Product Name :" + productName);
		//Assert.assertEquals(productName, "Md's Amazing pillow 2.0");
		softAssert.assertEquals(productName, "Md's Amazing pillow 2.0");
		
		String productDescription = jp.get("description");
		System.out.println("Product Description :" + productDescription);
		//Assert.assertEquals(productDescription, "The best pillow for amazing programmers");
		softAssert.assertEquals(productDescription, "The best pillow for amazing programmers");

		String productPrice = jp.get("price");
		System.out.println("Product Price :" + productPrice);
		//Assert.assertEquals(productPrice, "199");
		softAssert.assertEquals(productPrice, "199");

		softAssert.assertAll();
	}

}
