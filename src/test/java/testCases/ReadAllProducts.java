package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
public class ReadAllProducts {
	
	@Test
	public void readAllProducts() {
		
		Response response = 
		
		given()
		.baseUri("https://techfios.com/api-prod/api/product")
		.header("Content-Type","application/json; charset=UTF-8")
		.auth().preemptive().basic("demo@techfios.com","abc123").
		//.log().all()
		when()
		//.log().all()
		.get("/read.php").
		then()
		.extract().response();
		int statusCode = response.getStatusCode();
		System.out.println("Status Code:" + statusCode);
		Assert.assertEquals(statusCode, 200);
		long responseTime = response.getTime();
		System.out.println("ResponseTime:" +responseTime);
		if(responseTime <=2000) {
			
			System.out.println("Response time is within range");
		}else {
			System.out.println("Response time is out of range!");
		}
		
		
		//.statusCode(200).header("Content-Type", "application/json; charset=UTF-8");
		
		String responseHeaderContentType = response.getHeader("Content-Type");
		System.out.println("Response Header Content Type:" + responseHeaderContentType);
		Assert.assertEquals(responseHeaderContentType, "application/json; charset=UTF-8");
		String responseBody = response.getBody().asString();
		System.out.println("Response Body:" + responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
		String firstProductId = jp.get("records[0].id");
		System.out.println("First Product Id :" + firstProductId);
		
		if(firstProductId != null) {
			System.out.println("First Product Id is not null");
			
			
		}else {
			System.out.println("First Product Id is null");
		}
		
	}


}
