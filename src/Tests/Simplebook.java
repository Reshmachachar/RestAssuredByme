package Tests;

import Files.ReusableMethods;
import Files.SimplebookPayloads;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import static io.restassured.RestAssured.given;



public class Simplebook {
	
	public static String bearerToken;
	
	public static void main(String[] args) {
	    

	    RestAssured.baseURI="https://simple-books-api.glitch.me";
	//Token generation
	    JsonPath js = ReusableMethods.rawToJson(registerApi("Reshma","reshma@gmail.com"));
	     bearerToken=js.getString("accessToken");    
	     System.out.println("Token :"+bearerToken);
	     System.out.println("------------------------------------------------------------------");

	     //Postorder
	     JsonPath js1 = ReusableMethods.rawToJson(postOrder(4,"Reshma",bearerToken));
	       String orderid=js1.getString("orderId");
	     System.out.println("order id is"+orderid);
	     System.out.println("--------------------------------------------------------------------");
	     
	     //getAllorders
	     getAllOrder(bearerToken);
	     
	     //getSingleOrder
	     getsingleOrder(orderid,bearerToken);
	     
	     //UpdateOrderPayload
	     updateOrder(orderid, "VaishaliNandanwar", bearerToken);
	     getsingleOrder(orderid,bearerToken);
	     
	     //deleteOrder
	     deleteOrder(orderid,bearerToken );
	    
	}
	public static String registerApi(String name,String email)
	{    return
	    given()
	            .log().all()
	            .header("Content-Type","application/json")
	            .body(SimplebookPayloads.registerPayload(name,email))
	        .when()
	            .post("/api-clients/")
	        .then()
	            .log().all()
	            .assertThat().statusCode(201)
	            .extract().body().asString();
	}
	public static String postOrder(int bookId,String customerName,String bearerToken)
	{
	    return
	    given()
	    .auth().oauth2(bearerToken)
	    .headers("Content-Type","application/json")
	    .body(SimplebookPayloads.postOrderPayload(bookId,customerName))
	    .when()
	    .post("/orders/")
	    .then()
	    .log().all()
	    .assertThat().statusCode(201)
	    .extract().body().asString();
	}
	public static void getAllOrder(String bearerToken)
	{
	    given()
	    .auth().oauth2(bearerToken)        
	    .when()
	    .get("/orders")
	    .then()
	    .log().all()
	    .assertThat().statusCode(200);     
	}
	public static void getsingleOrder(String orderId,String bearerToken)
	{
	    given()
	    .auth().oauth2(bearerToken)
	    .when()
	    .get("/orders/{orderId}",orderId)
	    .then()
	    .log().all()
	    .assertThat().statusCode(200);  
	}
	public static void updateOrder(String orderId,String customerName,String bearer_Token)
	{

	    given()
	    .auth().oauth2(bearer_Token)
	    .headers("Content-Type","application/json")
	    .body(SimplebookPayloads.updateOrderPayload(customerName))
	    .when()
	    .patch("/orders/{orderId}",orderId)
	    .then()
	    .log().all()
	    .assertThat().statusCode(204);
	    
	}
	
	public static void deleteOrder(String orderId,String bearer_Token)
	{

	    given()
	    .auth().oauth2(bearer_Token)
	    .when()
	    .delete("/orders/{orderId}",orderId)
	    .then()
	    .log().all()
	    .assertThat().statusCode(204);
	}

}

