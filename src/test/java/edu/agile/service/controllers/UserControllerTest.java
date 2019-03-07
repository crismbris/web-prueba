package edu.agile.service.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jayway.jsonpath.JsonPath;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ResponseBody;
import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;

import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import edu.agile.service.App;

/**
 * Test class for UserController methods
 * @author ideagram
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = App.class)
@TestPropertySource(value = { "classpath:application.properties" })
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class UserControllerTest {
	int port = 8080;

	@Before
	public void setBaseUri() {
		RestAssured.port = port;
		RestAssured.baseURI = "http://localhost"; // replace as appropriate
	}
	
	/**
	 * <b>Case test code:</b> 5 <br>
	 * <b>Description:</b> Unit test for web service used for user self registration (i.e. sign up)
	 * 
	 * @result Verify if returned JSON includes required fields
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public void userSelfRegistration() {
		// Preparing JSON for post request body
		Map<String, Object> userRegBody = new HashMap<>();
		userRegBody.put("email", System.currentTimeMillis()+"@test.com");
		userRegBody.put("username", "The guardian Groot");
		userRegBody.put("password", "00112233");
		userRegBody.put("roles", "USER");
		
		// Doing the request
		Response response = given().contentType("application/json").body(userRegBody).when().post("/user/register");

		// Check success response
		int statusCode = response.getStatusCode();
		assertEquals(statusCode, 200);
		
		// Getting and parsing response
		ResponseBody resBody = response.getBody();
		String json = resBody.prettyPrint();
		LinkedHashMap<String, Object> jsonPostResponse = JsonPath.read(json, "$");
		
		// Validating if required fields are included in response
		assertNotNull(jsonPostResponse.get("id"));
		assertNotNull(jsonPostResponse.get("email"));
		assertNotNull(jsonPostResponse.get("username"));
		
		assertTrue(jsonPostResponse.get("email").toString().length()<=40);
		assertTrue(jsonPostResponse.get("username").toString().length()<=70 &&
				jsonPostResponse.get("username").toString().length()>=1);
	}
	
	/**
	 * <b>Case test code:</b> 7 <br>
	 * <b>Description:</b> Unit test for web service used to do user login in system
	 * 
	 * @result Verify if returned JSON includes required fields
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public void doLogin() {
		// Preparing request parameters
		String userEmail = "demo@demo.com";
		String userPassword = "demo";
		
		// Doing the request
		Response response = get("/user/login/"+userEmail+"/"+userPassword);
		ResponseBody resBody = response.getBody();
		
		// Check success response
		int statusCode = response.getStatusCode();
		assertEquals(statusCode, 200);
		
		// Getting and parsing response
		String json = resBody.prettyPrint();
		LinkedHashMap<String, Object> jsonResponse = JsonPath.read(json, "$");
		
		// Check if required fields are returned
		assertNotNull(jsonResponse.get("id"));
		assertNotNull(jsonResponse.get("email"));
		assertNotNull(jsonResponse.get("username"));
	}
}
