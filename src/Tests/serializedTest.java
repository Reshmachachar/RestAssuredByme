package Tests;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;
import POJO.AddPlace;
import POJO.GetCourses;
import POJO.location;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;

public class serializedTest {

	public static void main(String[] args) {
		
		AddPlace p=new AddPlace();
		p.setAccuracy("50");
		p.setName("Frontline house");
		p.setPhone_number("(+91) 983 893 3937");
		p.setAddress("29, side layout, cohen 09");
		
		p.setWebsite("http://google.com");
		p.setLanguage("French-IN");
		location l=new location();
		l.setLat("-38.383494");
		l.setLng("33.427362");
		p.setLocation(l);
		
		List<String> a=new ArrayList<String>();
		a.add("shoe park");
		a.add("shop");
		p.setTypes(a);
		
		//RestAssured
		RestAssured.baseURI="https://rahulshettyacademy.com";
	String addPlaceResponse=
		given()
		.log().all()
		.queryParam("key"," qaclick123")
		.header("Content-Type","application/json")
		.body(p)
	.when()
	     .post("/maps/api/place/add/json")
	 .then()
	      .assertThat().statusCode(200)
	      .extract().response().asString();
		
		System.out.println(addPlaceResponse);
	
//	//Assignment
			given()
			.queryParam("key"," qaclick123")
			.expect().defaultParser(Parser.JSON)
			.when()
			.get("https://rahulshettyacademy.com/getCourse.php")
			.as(GetCourses.class);
	System.out.println();
	System.out.println();
	
	
		
	}
}
