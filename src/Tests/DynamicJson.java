package Tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import Files.ReusableMethods;
import Files.payLoads;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class DynamicJson {

		//Add Book
		//given - all input details
		//when - http method + resources
		//then - assertions
	@Test(dataProvider="addBookData")	
	public void addBook(String isbn,String aisle)
	{
		RestAssured.baseURI = "http://216.10.245.166";
			String addBookResponse =	
			given()
			.log().all()
			.header("Content-Type", "application/json")
			.body(payLoads.addBookPayload(isbn,aisle))
	.when()
		.post("/Library/Addbook.php")
	.then()
		.log().all()
		.assertThat().statusCode(200)
		.header("Server", "Apache")
	    .extract().response().asString();
			
			JsonPath js =ReusableMethods.rawToJson(addBookResponse);
			String id = js.getString("ID");	
			System.out.println(id);
	}		
	@DataProvider(name="addBookData")
	public Object[][] getData()
	{
		return new Object[][] {{"xyz","123"},{"yzx","567"},{"xzr","987"}};
	}
	
//			//getBook by ID
//			String getBookResponseById=
//			given()
//				.log().all()
//				.queryParam("ID", id)
//				
//			
//			.when()
//				.get("/Library/GetBook.php")
//				
//			.then()
//				.log().all()
//				.assertThat().statusCode(200)
//				.extract().response().asString();
//			
//			//parse json
//			JsonPath js1 =ReusableMethods.rawToJson(getBookResponseById);
//			String actualBookId = js.getString("ID");	
//			System.out.println(actualBookId);
//			Assert.assertEquals(id , actualBookId);
//			
//			
//			//getBook by Author
//			String getBookResponseByAuthor=
//					given()
//				     .log().all()
//				     .queryParam("AuthorName", "John foe")
//				     
//				.when()
//				  .get("/Library/GetBook.php")
//				  
//				.then()
//				    .log().all()
//				    .assertThat().statusCode(200)
//				    .extract().response().asString();
//			
//				JsonPath js2= ReusableMethods.rawToJson(getBookResponseByAuthor);
//				String actualAuthor=js2.getString("author");
//				//Assert.assertEquals(actualAuthor,"John foe");
			
}


