package testCases;

import java.io.File;
import java.io.FileOutputStream;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.RestAssured;

public class DownloadFile {
	SoftAssert softAssert;
	
	public static long getFileSize() {
		File baseFile = new File("./files/ReprogramCareer.png");
		return baseFile.length();
	}
	
	@Test
	void test() {
		softAssert = new SoftAssert();
		
		byte[] dowloadedFile = RestAssured.given().when()
								.get("https://personal.utdallas.edu/~jxc064000/emse/Graphics/ReprogramCareer.png")
								.then().extract().asByteArray();

		System.out.println("Actual File Size : " + getFileSize());
		System.out.println("Download File Size : " + dowloadedFile.length);
		softAssert.assertEquals(getFileSize(), dowloadedFile.length, "File side did not match");
		
		try {
			FileOutputStream os = new FileOutputStream(new File("./files/ReprogramCareer.png"));
			os.write(dowloadedFile);
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
