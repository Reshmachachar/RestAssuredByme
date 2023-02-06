package Tests;

import org.testng.Assert;

import Files.ReusableMethods;
import Files.payLoads;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {
	
	public static void main(String args[] ) {
	
	JsonPath js = ReusableMethods.rawToJson(payLoads.getCoursesPayLoad());
	
	//1. Print No of courses returned by API
	int courseCount = js.getInt("courses.size()");
	System.out.println("No of courses :" +courseCount);
	
	//2.Print Purchase Amount
	int purchaseAmt = js.getInt("dashboard.purchaseAmount");
	System.out.println("purchase amount is :" +purchaseAmt);
	
	//3. Print Title of the first course
	String firstCourseTitle = js.getString("courses[0].title");
	System.out.println("first course title is :" +firstCourseTitle);
	
	System.out.println("-------------------------------------------------------------------");
	
	//4. Print All course titles and their respective Prices
	for(int i=0;i<courseCount;i++)
	{
		String courseTitles = js.getString("courses["+i+"].title");
		System.out.println(courseTitles);
		System.out.println(js.getInt("courses["+i+"].price"));
	}
	
	System.out.println("-------------------------------------------------------------------");
	
	//5. Print no of copies sold by RPA Course
	for(int i=0;i<courseCount;i++)
	{
		String courseTitles = js.getString("courses["+i+"].title");
		if(courseTitles.equals("RPA"))
		{
			int copiesRPA = js.getInt("courses["+i+"].copies");
			System.out.println("No. of RPA copies are :" +copiesRPA);
			
		}
	}
	
	System.out.println("-------------------------------------------------------------------");
	
	//6. Verify if Sum of all Course prices matches with Purchase Amount
	
	
	int coursePrice = 0;
	for(int i=0;i<courseCount;i++)
	{
		int Price = js.getInt("courses["+i+"].price");
		int Copies = js.getInt("courses["+i+"].copies");
		coursePrice = coursePrice + (Price*Copies);
	}
	System.out.println("Sum of all course price is: " +coursePrice);
	
	if(purchaseAmt==coursePrice)
	{
		System.out.println("Amount matched");
	}
	Assert.assertEquals(purchaseAmt, coursePrice);
	}
}
