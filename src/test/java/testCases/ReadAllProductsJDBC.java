package testCases;

import static io.restassured.RestAssured.given;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ReadAllProductsJDBC {
	
	private Connection connection = null;
    private Statement statement = null;
    private ResultSet resultset = null;
    
	String sqlUrl = "jdbc:mysql://localhost:3306/rest_assured_db";
	String sqlUsername = "root";
	String sqlPassword = "root123";
	String sqlquery = "Select * from createpayload;";
    
	@Test
public void readAllProducts() throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		// Create connection to the local database
		connection = DriverManager.getConnection(sqlUrl, sqlUsername, sqlPassword);

		// Empower connection reference variable to execute queries
		statement = connection.createStatement();

		// Deliver the query
		resultset = statement.executeQuery(sqlquery);

		resultset.next();
		String name = resultset.getString("name");
		
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
