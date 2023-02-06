package POJO;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;
import POJO.AddPlace;
import POJO.GetCourses;
import POJO.location;
import io.restassured.parsing.Parser;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
public class SpecBuilderTest {
	
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
			//RestAssured.baseURI="https://rahulshettyacademy.com";
			RequestSpecification req=new RequestSpecBuilder()
					.setBaseUri("https://rahulshettyacademy.com")
					.addQueryParam("key","qaclick123")
					.setContentType(ContentType.JSON)
					.build();
			
			ResponseSpecification res=new ResponseSpecBuilder()
					.expectStatusCode(200)
					.expectContentType(ContentType.JSON)
					.build();
          RequestSpecification addPlaceRequest = given()
			.log().all()
			.spec(req)
			.body(p);
			 
			 addPlaceResponsePOJO apr=addPlaceRequest
		.when()
		     .post("/maps/api/place/add/json")
		 .then()
		      .spec(res)
		      .extract().response().as(addPlaceResponsePOJO.class);
			
			System.out.println("status"+apr.getStatus());
			System.out.println("place_id"+apr.getPlace_id());
			System.out.println("reference"+apr.getReference());
			System.out.println("scope"+apr.getScope());
			System.out.println("id"+apr.getId());
	
			
		}
	}


