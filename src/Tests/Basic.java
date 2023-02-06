package Tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matcher.*;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.Assert;

import Files.ReusableMethods;
import Files.payLoads;

public class Basic {
	
	//static String updateAdd = "70 winter walk, USA";

	public static void main(String[] args) throws IOException {
		
		//AddPlace
		//given - all input details
		//when - http method + resources
		//then - assertions
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
			String addPlaceResponse =	
			given()
			.log().all()
			.queryParam("key","qaclick123")
			.header("Content-Type", "application/json")
			//.body(payLoads.addPlacePayload())

			//add payloads from file
			.body(new String(Files.readAllBytes(Paths.get("F:\\material\\addBookPayloads.json"))))
			.when()
				.post("/maps/api/place/add/json")
				
			.then()
				.log().all()
				.assertThat().statusCode(200)
				.body("scope", equalTo("APP"))
				.header("Server", "Apache/2.4.41 (Ubuntu)")
				
				.extract().response().asString();
			
			
			//Update place with new address
			
			//parse json response of add place & store placeId
			JsonPath js = new JsonPath(addPlaceResponse);
			String placeId = js.getString("place_id");
			System.out.println(placeId);
			
			
			
			//UpdatePlace
			String newAddress = "70 winter walk, USA";
			given()
			.log().all()
			.queryParam("key", "qaclick123")
			.queryParam("place_id", placeId)
			.header("Content-Type", "application/json")
			.body("{\r\n"
					+ "\"place_id\":\""+placeId+"\",\r\n"
					+ "\"address\":\""+newAddress+"\",\r\n"
					+ "\"key\":\"qaclick123\"\r\n"
					+ "}")
			.when()
				.put("/maps/api/place/update/json")
				
			.then()
				.log().all()
				.assertThat().statusCode(200)
				.body("msg", equalTo("Address successfully updated"))
				.extract().response().asString();
				
			
			
			
			
			//getPlace
			String getPlaceResponse =
			given()
				.log().all()
				.queryParam("key", "qaclick123")
				.queryParam("place_id", placeId)
			
			.when()
				.get("/maps/api/place/get/json")
				
			.then()
				.log().all()
				.assertThat().statusCode(200)
				.extract().response().asString();
			
			//parse json
			//JsonPath js1 = new JsonPath(getPlaceResponse);
			JsonPath js1 = ReusableMethods.rawToJson(getPlaceResponse);
			String actualAddress = js1.getString("address");
			Assert.assertEquals(newAddress , actualAddress);
			

	}

}
