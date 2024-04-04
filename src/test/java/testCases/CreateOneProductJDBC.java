package testCases;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.response.Response;

public class CreateOneProductJDBC {
	String baseURI;
	String firstProductId;
	HashMap<String, String> createPayload;

	private Connection connection = null;
	private Statement statement = null;
	private ResultSet resultset = null;

	String sqlUrl = "jdbc:mysql://localhost:3306/rest_assured_db";
	String sqlUsername = "root";
	String sqlPassword = "root123";
	String sqlquery = "Select * from createpayload;";

	public CreateOneProductJDBC() {
		baseURI = "https://techfios.com/api-prod/api/product";
		createPayload = new HashMap<String, String>();
	}

	@Test(priority = 1)
	public void createOneProduct() throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver");
		connection = DriverManager.getConnection(sqlUrl, sqlUsername, sqlPassword);
		statement = connection.createStatement();
		resultset = statement.executeQuery(sqlquery);
		// Next line will read each row at a time
		resultset.next();
		///getting data from database and storing it into hashMap.
		createPayload.put("name", resultset.getString("name"));
		createPayload.put("price", resultset.getString("price"));
		createPayload.put("description", resultset.getString("description"));
		createPayload.put("category_id", resultset.getString("category_id"));
		createPayload.put("category_name", resultset.getString("category_name"));
		
		//String name = resultset.getString("name");
		//String price = resultset.getString("price");
		//String description = resultset.getString("description");

		Response response = 
				given()
				.baseUri(baseURI)
				.header("Content-Type", "application/json; charset=UTF-8")
				.header("Authorization", "Bearer dskjfhsdkjfhds66#$%dsfhdhfh").body(createPayload).when()
				.post("/create.php").then().extract().response();

		String responseBody = response.getBody().asString();
		System.out.println("Response Body:" + responseBody);
	}

}
