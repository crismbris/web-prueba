package edu.agile.service.controllers;

import static org.hamcrest.Matchers.*;
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
import static com.jayway.restassured.RestAssured.when;
import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;

import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import edu.agile.service.App;
import net.minidev.json.JSONArray;

/**
 * Test class for IdeaController methods
 * @author ideagram
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = App.class)
@TestPropertySource(value = { "classpath:application.properties" })
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class IdeaControllerTest {

	int port = 8080;

	@Before
	public void setBaseUri() {
		RestAssured.port = port;
		RestAssured.baseURI = "http://localhost"; // replace as appropriate
	}

	/**
	 * <b>Case test code:</b> 1 <br>
	 * <b>Description:</b> Unit test for web service that response should
	 * 				be the list of registered ideas.
	 * 
	 * @result Verify if returned JSON Array elements includes 
	 * 		   required fields
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void getListOfIdeas() {
		// Doing the request
		ResponseBody resBody = get("/ideas/").getBody();
		
		// Getting and parsing response
		String json = resBody.prettyPrint();
		JSONArray jsonArrayResponse = (JSONArray) JsonPath.read(json, "$.[*]");
		LinkedHashMap<String, Object> jObj = (LinkedHashMap<String, Object>) jsonArrayResponse
				.get(jsonArrayResponse.size() - 1);

		// Check if list response has at least one element
		assertTrue(jsonArrayResponse.size() > 0);

		// Check if required fields are returned
		assertNotNull(jObj.get("id"));
		assertNotNull(jObj.get("title"));
		assertNotNull(jObj.get("description"));
		assertNotNull(jObj.get("sale_mode"));
		assertNotNull(jObj.get("price"));
		assertNotNull(jObj.get("idCategories"));
	}

	/**
	 * <b>Case test code:</b> 2 <br> 
	 * <b>Description:</b> Unit test of web service which inserts a new
	 * idea in data base
	 * 
	 * @result Verify if returned JSON includes required fields, also after test
	 *         post request executes a get request for ensure the returned data is
	 *         really registered in database
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public void createNewIdea() {
		// Preparing request parameters
		LinkedHashMap<String, Integer> catElement = new LinkedHashMap<String, Integer>();
		catElement.put("categoryId", Integer.valueOf("2"));

		JSONArray jArray = new JSONArray();
		jArray.add(catElement);

		Map<String, Object> createIdeaBody = new HashMap<>();
		createIdeaBody.put("title", "Title Test " + System.currentTimeMillis());
		createIdeaBody.put("description", "Agile Methods");
		createIdeaBody.put("saleMode", 2);
		createIdeaBody.put("price", 12000);
		createIdeaBody.put("ideaCategoryCollection", jArray);

		// Doing the request
		Response response = given().contentType("application/json").body(createIdeaBody).when().post("/ideas/add");

		// Check success response
		int statusCode = response.getStatusCode();
		assertEquals(statusCode, 200);

		// Getting and parsing response
		ResponseBody resBody = response.getBody();
		String json = resBody.prettyPrint();
		LinkedHashMap<String, Object> jsonPostResponse = JsonPath.read(json, "$");

		// Check if Post response contains all required fields
		assertNotNull(jsonPostResponse.get("id"));
		assertNotNull(jsonPostResponse.get("title"));
		assertNotNull(jsonPostResponse.get("description"));
		assertNotNull(jsonPostResponse.get("sale_mode"));
		assertNotNull(jsonPostResponse.get("price"));
		assertNotNull(jsonPostResponse.get("idCategories"));

		// Get the id of inserted idea for verification request
		int insertedId = (int) jsonPostResponse.get("id");

		// Doing the verification request
		response = get("/ideas/" + insertedId);
		
		// Check verification request success response
		statusCode = response.getStatusCode();
		assertEquals(statusCode, 200);

		// Getting and parsing response of verification request
		ResponseBody verificationResBody = response.getBody();
		json = verificationResBody.prettyPrint();
		LinkedHashMap<String, Object> jsonResponse = JsonPath.read(json, "$");
		
		// Check if verification request response contains all required fields
		assertNotNull(jsonResponse.get("id"));
		assertNotNull(jsonResponse.get("title"));
		assertNotNull(jsonResponse.get("description"));
		assertNotNull(jsonResponse.get("sale_mode"));
		assertNotNull(jsonResponse.get("price"));
		assertNotNull(jsonResponse.get("idCategories"));
	}

	/**
	 * <b>Case test code:</b> 3 <br>
	 * <b>Description:</b> Unit test of web service which get the detail
	 * of a given idea ID
	 * 
	 * @result Verify if returned JSON includes required fields
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public void getIdeaDetails() {
		// Doing the request
		Response response = get("/ideas/" + 25);

		// Check success response
		int statusCode = response.getStatusCode();
		assertEquals(statusCode, 200);

		// Getting and parsing response
		ResponseBody verificationResBody = response.getBody();
		String json = verificationResBody.prettyPrint();
		LinkedHashMap<String, Object> jsonResponse = JsonPath.read(json, "$");

		// Check if response contains all required fields
		assertNotNull(jsonResponse.get("id"));
		assertNotNull(jsonResponse.get("title"));
		assertNotNull(jsonResponse.get("description"));
		assertNotNull(jsonResponse.get("sale_mode"));
		assertNotNull(jsonResponse.get("price"));
		assertNotNull(jsonResponse.get("idCategories"));
	}

	/**
	 * <b>Case test code:</b> 4 <br>
	 * <b>Description:</b> Unit test of web service which receives an idea ID and a vote that could be 1 or -1.
	 * 
	 * @result Verify if returned JSON includes required fields and if the given idea ID just conserve one vote value per user.
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public void voteIdea() {
		// Preparing request parameters
		Map<String, Object> voteIdeaBody = new HashMap<>();

		// Doing the request
		Response response = given().contentType("application/json").body(voteIdeaBody).when().post("/ideas/25/vote/1");

		// Check success response
		int statusCode = response.getStatusCode();
		assertEquals(statusCode, 200);

		// Getting and parsing response
		ResponseBody verificationResBody = response.getBody();
		String json = verificationResBody.prettyPrint();
		LinkedHashMap<String, Object> jsonResponse = JsonPath.read(json, "$");

		// Check if response contains all required fields
		assertNotNull(jsonResponse.get("id"));
		assertNotNull(jsonResponse.get("user_id"));
		assertNotNull(jsonResponse.get("idea_id"));
		assertEquals(jsonResponse.get("idea_id"), 25);		
		assertNotNull(jsonResponse.get("vote"));
		assertEquals(jsonResponse.get("vote"), 1);

		// Doing the request for second vote value
		response = given().contentType("application/json").body(voteIdeaBody).when().post("/ideas/25/vote/-1");

		// Check success response
		statusCode = response.getStatusCode();
		assertEquals(statusCode, 200);

		// Getting and parsing response
		verificationResBody = response.getBody();
		json = verificationResBody.prettyPrint();
		jsonResponse = JsonPath.read(json, "$");

		// Check if response contains all required fields
		assertNotNull(jsonResponse.get("id"));
		assertNotNull(jsonResponse.get("user_id"));
		assertNotNull(jsonResponse.get("idea_id"));
		assertEquals(jsonResponse.get("idea_id"), 25);
		assertNotNull(jsonResponse.get("vote"));
		assertEquals(jsonResponse.get("vote"), -1);
	}

	/**
	 * <b>Case test code:</b> 6 <br>
	 * <b>Description:</b> Unit test of web service which receives a category ID and returns all ideas that have it.
	 * 
	 * @result Verify if returned JSON includes required fields
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void getIdeaListFilteredByCategory() {
		// Preparing request parameters
		int categoryId = 1;
		
		// Doing the request
		Response response = when().get("/ideas/category/" + categoryId);
		// Verifying if all returned elements contains the queried category
		response.then().body("idCategories", everyItem(hasItem(categoryId)));
		
		// Getting and parsing response
		ResponseBody resBody = response.getBody();
		String json = resBody.prettyPrint();
		JSONArray jsonArrayResponse = (JSONArray) JsonPath.read(json, "$.[*]");
		LinkedHashMap<String, Object> jObj = 
				(LinkedHashMap<String, Object>) jsonArrayResponse.get(jsonArrayResponse.size() - 1);

		// Check if list response has at least one element
		assertTrue(jsonArrayResponse.size() > 0);

		// Check if required fields are returned
		assertNotNull(jObj.get("id"));
		assertNotNull(jObj.get("title"));
		assertNotNull(jObj.get("description"));
		assertNotNull(jObj.get("sale_mode"));
		assertNotNull(jObj.get("price"));
		assertNotNull(jObj.get("idCategories"));
		
	}

}