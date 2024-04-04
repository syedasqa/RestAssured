package testCases;

import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;

import java.io.File;

public class FileUploadExample {
	@Test
	public void testDocuments() {
		String baseURI = "https://techfios.com/api-prod/api";
	    // Test post a document.
	    given().multiPart("file", new File("src/main/java/data/CreatePayload.json", "text/html"))
	            .expect().statusCode(201).when()
	            .post(baseURI + "/product");
	}
}
