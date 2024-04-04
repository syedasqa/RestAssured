package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import java.io.File;

public class UpdateOneProduct {

	String baseURI;
	SoftAssert softAssert;
	String updatePayloadFilePath;
	
	public UpdateOneProduct() {
		baseURI = "https://techfios.com/api-prod/api/product";
		softAssert = new SoftAssert();
		updatePayloadFilePath = "src\\main\\java\\data\\UpdatePayload.json";
	}

	@Test
	public void updateOneProducts() {

		Response response =

				given()
				.baseUri(baseURI)
				.header("Content-Type", "application/json; charset=UTF-8")
						.header("Authorization", "Bearer dskjfhsdkjfhds66#$%dsfhdhfh")
						.body(new File(updatePayloadFilePath)).
						when()
						.put("/update.php").
						then()
						.extract().response();
		
		int statusCode = response.getStatusCode();
		System.out.println("Status Code:" + statusCode);
		softAssert.assertEquals(statusCode, 200, "Status code are not matching!");
		
		long responseTime = response.getTime();
		System.out.println("ResponseTime:" + responseTime);
		if (responseTime <= 2000) {

			System.out.println("Response time is within range");
		} else {
			System.out.println("Response time is out of range!");
		}

		String responseHeaderContentType = response.getHeader("Content-Type");
		System.out.println("Response Header Content Type:" + responseHeaderContentType);
		softAssert.assertEquals(responseHeaderContentType, "application/json; charset=UTF-8");

		String responseBody = response.getBody().asString();
		System.out.println("Response Body:" + responseBody);

		JsonPath jp = new JsonPath(responseBody);
		String productMessage = jp.get("message");
		System.out.println("Product Message :" + productMessage);
		softAssert.assertEquals(productMessage, "Product was updated.", "product Message are not matching!");
	}
		
		@Test(priority = 3)
		public void readOneProducts() {
			JsonPath jp2 = new JsonPath(new File(updatePayloadFilePath));
			String updateProductId = jp2.get("id");
			Response response = given().baseUri(baseURI).header("Content-Type", "application/json")
					.header("Authorization", "Bearer dskjfhsdkjfhds66#$%dsfhdhfh")
					.queryParam("id", updateProductId).
					when()
					.get("/read_one.php").then().extract().response();

			
			String expectedProductName = jp2.get("name");
			String expectedProductDescription = jp2.get("description");
			String expectedProductPrice = jp2.get("price");

			String responseBody = response.getBody().asString();
			System.out.println("Response Body:" + responseBody);

			JsonPath jp = new JsonPath(responseBody);
			String updatedProductName = jp.get("name");
			System.out.println("Updated Product Name :" + updatedProductName);
			softAssert.assertEquals(updatedProductName, expectedProductName, "Products Names are not matching!");

			String updatedProductDescription = jp.get("description");
			System.out.println("Updated Product Description :" + updatedProductDescription);
			softAssert.assertEquals(updatedProductDescription, expectedProductDescription,
					"Products Descriptions are not matching");

			String updatedProductPrice = jp.get("Price");
			System.out.println("Updated Product Price :" + updatedProductPrice);
			softAssert.assertEquals(updatedProductPrice, expectedProductPrice, "Products Prices are not matching!");


		softAssert.assertAll();
	}

}
