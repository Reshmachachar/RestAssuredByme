package Tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matcher.*;
import static org.hamcrest.Matchers.equalTo;

import Files.ReusableMethods;
import Files.payLoads;

public class SimpleBookApi {

	public static void main(String[] args) {
		
		//-------------------------get status---------------------------------
		RestAssured.baseURI = "https://simple-books-api.glitch.me";
		given()
			.log().all()
			
		.when()
			.get("/status")
			
		.then()
			.log().all()
			.assertThat().statusCode(200)
			.body("status",equalTo("OK"));
		
		//------------------------get list of books---------------------------------
			given()
				.log().all()
		
			.when()
				.get("/books")
		
			.then()
				.log().all()
				.assertThat().statusCode(200);
			
		//-----------------------get single book---------------------------------
			String id = "4";
			given()
			.log().all()
			//.pathParam("bookId", id)
			//.queryParam("bookId", id)
	
		.when()
			.get("/books/4")
	
		.then()
			.log().all()
			.assertThat().statusCode(200);
			
		//-----------------------Register APi Client-------------------------------------
			String AccessToken =
				given()
					.log().all()
					.header("Content-Type", "application/json")
					.body(payLoads.registerAiClient("vaishaliN", "vaishali72@gmail.com"))
					
				.when()
					.post("/api-clients/")
					
				.then()
					.log().all()
					.assertThat().statusCode(201)
					.extract().response().asString();
			
			JsonPath js = ReusableMethods.rawToJson(AccessToken);
			String token = js.getString("accessToken");
			System.out.println("Token is : "+token);
			
			//-------------------post Submit an order-----------------------------
			String orderID =given()
			.log().all()
			.header("Content-Type", "application/json")
			.header("Authorization", ""+token+"")
			.body(payLoads.submitOrder(4, "vaishaliNN"))
			
			.when()
				.post("/orders")
				
			.then()
				.log().all()
				.assertThat().statusCode(201)
				.extract().response().asString();
			
			JsonPath js1 = ReusableMethods.rawToJson(orderID);
			String orderid = js1.getString("orderId");
			System.out.println("Order id  is : "+orderid);
			
			
			//----------------------get all orders------------------------------
			given()
			.log().all()
			.header("Content-Type", "application/json")
			.header("Authorization", ""+token+"")
			
			.when()
				.get("/orders")
				
			.then()
				.log().all()
				.assertThat().statusCode(200);
			
			
			//-----------------------------Get an order-------------------------------

	        given()
	        .log().all()
	            .header("Authorization", ""+token+"")
	        .when()
	             .get("/orders/"+orderid+"")
	        .then()
	             .log().all()
	             .assertThat().statusCode(200);
			 
			//-------------------------------Update an order-------------------------    
			    given()
			    .log().all()
			    .header("Authorization", ""+token+"")
			    .body("{\r\n" + 
			            "  \"customerName\": \"Vaishali\"\r\n" + 
			            "}")
			    .when()
			        .patch("/orders/"+orderid+"")
			    
			    .then()
			        .log().all()
			        .assertThat().statusCode(204);
				

	}

}
