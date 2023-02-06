package Tests;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.io.File;

import org.testng.Assert;

import Files.ReusableMethods;
public class JiraTestApi {
	public static void main(String[] args) {
		
		//Login
		RestAssured.baseURI= "http://localhost:8080";
		SessionFilter sFilter= new SessionFilter();
		
		given()
		.log().all()
		.header("Content-Type","application/json")
		.body("{\r\n"
				+ "    \"username\": \"Reshma.chachar\",\r\n"
				+ "    \"password\": \"Reshma@123\"\r\n"
				+ "}")
		.filter(sFilter)
		.when()
		.post("/rest/auth/1/session")
		.then()
		.log().all()
		.extract().response().asString();
		
		
		// create issue
		String idResponse=
		given()
		.log().all()
		.header("Content-Type", "application/json")
		.body("{\r\n"
				+ "    \"fields\": {\r\n"
				+ "       \"project\":\r\n"
				+ "       {\r\n"
				+ "          \"key\": \"REST01\"\r\n"
				+ "       },\r\n"
				+ "       \"summary\": \"This is my first issue in created in RestAssuredAutomation\",\r\n"
				+ "       \"description\": \"This is my test description created through Postman\",\r\n"
				+ "       \"issuetype\": {\r\n"
				+ "          \"name\": \"Task\"\r\n"
				+ "       }\r\n"
				+ "   }\r\n"
				+ "}")
		.filter(sFilter)
		.when()
		.post("/rest/api/2/issue/")
		.then()
		.log().all()
		.assertThat().statusCode(201)
		.extract().response().asString();
		
		JsonPath js2 = ReusableMethods.rawToJson(idResponse);
		String ID= js2.getString("id");
		
		
		//Add Comment
		
		String expComment = "Hi how are you";
		
		String addCommentResponse =
				given()
				.log().all()
				.header("Content-Type", "application/json")
				.pathParam("key", ""+ID+"")
				.body("{\r\n"
						+ "    \"body\": \""+expComment+"\"\r\n"
						+ "}")
				.filter(sFilter)
				.when()
				.post("/rest/api/2/issue/{key}/comment")
				.then()
				.log().all()
				.assertThat().statusCode(201)
				.extract().response().asString();
		
		JsonPath js = ReusableMethods.rawToJson(addCommentResponse);
		String addCommentId = js.getString("id");   //getting id of added comment i.e. 10100
		
		
		//Add Attachment
		
		given()
		.log().all()
		.filter(sFilter)
		.pathParam("key", ""+ID+"")
		.header("X-Atlassian-Token" , "no-check")
		.header("Content-Type", "multipart/form-data")
		.multiPart("file", new File("F:\\RestAssuredApi\\WorkSpace\\RADemoProject\\src\\Tests\\myFile.txt"))
		.when()
		.post("/rest/api/2/issue/{key}/attachments")
		.then()
		.log().all()
		.assertThat().statusCode(200);
		
		//get Issue
		
		String getIssueResponse=
				given()
				.log().all()
				.filter(sFilter)
				.pathParam("key", ""+ID+"")
				.queryParam("fields", "comment")
				.when()
				.get("/rest/api/2/issue/{key}")
				.then()
				.log().all()
				.extract().response().asString();
		
		JsonPath js1 = ReusableMethods.rawToJson(getIssueResponse);
		String issueComment = js1.getString("comment");
		
		//Find Count 
				int commentCount = js1.getInt("fields.comment.comments.size()");
				System.out.println("total course count is"+commentCount);

				for(int i=0;i<commentCount;i++)
				{

					
					if(js1.getString("fields.comment.comments["+i+"].id").equals(addCommentId))
					{

						String actualComm=js1.getString("fields.comment.comments["+i+"].body");
						Assert.assertEquals(expComment,actualComm);
						System.out.println(actualComm);
					}
				}

	}

}
