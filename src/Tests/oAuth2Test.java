package Tests;

import static io.restassured.RestAssured.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import com.github.dockerjava.api.model.Driver;

import POJO.GetCourses;
import POJO.api;
import POJO.webAutomation;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.managers.ChromeDriverManager;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;

public class oAuth2Test {

	public static void main(String[] args) throws InterruptedException {

		/*System.setProperty("webdriver.chrome.driver","./drivers/chromedriver.exe");                                                                                            
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();*/

		WebDriver driver;
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

		driver.findElement(By.xpath("//input[@id='identifierId']")).sendKeys("reshma.chachar@cogniwize.com");
		driver.findElement(By.xpath("//span[text()='Next']")).click();
		driver.findElement(By.name("password")).sendKeys("Reshma@123");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//span[text()='Next']")).click();
		Thread.sleep(3000);
		String Url= driver.getCurrentUrl();

		System.out.println(Url);

		//String Url = "https://rahulshettyacademy.com/getCourse.php?code=4%2F0AWtgzh4s_JzgUqE_N8O13Hlr5SjdccVYciBN00zvWObbPJ_6aYhQmFtr4j3N9N_vw8Xb6g&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=1&prompt=none";
		String partialUrl = Url.split("code=")[1];
		String code = partialUrl.split("&scope")[0];

		//getAccessToken
		String getAccessToken = given()
				.log().all()
				.urlEncodingEnabled(false)
				.queryParams("code",code)
				.queryParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
				.queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
				.queryParams("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
				.queryParams("grant_type", "authorization_code")
				//.queryParams("scope", "https://www.googleapis.com/auth/userinfo.email")
				.when()
				.post("https://www.googleapis.com/oauth2/v4/token")
				.asString();

		System.out.println(getAccessToken);

		JsonPath js = new JsonPath(getAccessToken);
		String accessToken= js.getString("access_token");
		System.out.println(accessToken);


		//getCourse   a(java object)->request body (serialization)(setter method)
		             // response body ->java object (deserialization) (getter method )
		GetCourses gc =
				given()
				.queryParam("access_token", accessToken)
				.expect().defaultParser(Parser.JSON)
				.when()
				.get("https://rahulshettyacademy.com/getCourse.php")
				.as(GetCourses.class);
		System.out.println(gc.getLinkedIn());
		System.out.println(gc.getInstructor());

	//	System.out.println(getCourseResponse);
		
		//to print prize from Api
		System.out.println(gc.getCourses().getApi().get(1).getPrice());
 
		List<api> courses = gc.getCourses().getApi();
		for(int i=0;i<courses.size();i++)
		{
			if(courses.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing"))
			{
				System.out.println(courses.get(i).getPrice());
			}
		}
		
		//print all courses under web automation 
		
//		List<webAutomation> webAuto = gc.getCourses().getWebAutomation();
//		
//		for(int i=0;i<webAuto.size();i++)
//		{
//			System.out.println(webAuto.get(i).getCourseTitle());
//			System.out.println(webAuto.get(i).getPrice());
//		}
		
		String [] expTitle= {"Selenium Webdriver Java","Cypress","Protractor"};
		ArrayList<String> actTitle=new ArrayList<String>();
		 List<webAutomation> a=gc.getCourses().getWebAutomation();
		 
		 for(int j=0;j<a.size();j++)
		 {
			 actTitle.add(a.get(j).getCourseTitle());
		 }
		 List<String> finalexpTitle = Arrays.asList(expTitle);
		 Assert.assertTrue(finalexpTitle.equals(actTitle));
		 }
		
		
	}


