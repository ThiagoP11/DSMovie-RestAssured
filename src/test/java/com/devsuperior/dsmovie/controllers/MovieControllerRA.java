package com.devsuperior.dsmovie.controllers;

import com.devsuperior.dsmovie.tests.TokenUtil;
import io.restassured.http.ContentType;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class MovieControllerRA {

	private String clientUsername, clientPassword, adminUsername, adminPassword, adminOnlyUsername, adminOnlyPassword;
	private String clientToken, adminToken, adminOnlyToken, invalidToken;
	private Long existindMovieId, nonExistindMovieId;
	private String title;

	private Map<String, Object> item = new HashMap<>();

	@BeforeEach
	public void setup() throws Exception {
		baseURI = "http://localhost:8080";
		existindMovieId = 1L;
		nonExistindMovieId = 100L;
		title = "The Witcher";

		clientUsername = "joaquim@gmail.com";
		clientPassword = "123456";
		adminUsername = "alex@gmail.com";
		adminPassword = "123456";
		adminOnlyUsername = "ana@gmail.com";
		adminOnlyPassword = "123456";

		clientToken = TokenUtil.obtainAccessToken(clientUsername, clientPassword);
		adminToken = TokenUtil.obtainAccessToken(adminUsername, adminPassword);
		adminOnlyToken = TokenUtil.obtainAccessToken(adminOnlyUsername, adminOnlyPassword);
		invalidToken = adminToken + "xpto";


		item.put("title", "Test Movie");
		item.put("score", 0.0);
		item.put("count", 0);




	}

	@Test
	public void findAllShouldReturnOkWhenMovieNoArgumentsGiven() {
		given()
				.get("/movies?page=0")
				.then()
				.statusCode(200)

				// tamanho da lista
				.body("content.size()", greaterThan(0))

				// primeiro item
				.body("content[0].id", equalTo(1))
				.body("content[0].title", equalTo("The Witcher"))
				.body("content[0].score", equalTo(4.5f))
				.body("content[0].count", equalTo(2))
				.body("content[0].image", containsString("themoviedb.org"));
	}
	
	@Test
	public void findAllShouldReturnPagedMoviesWhenMovieTitleParamIsNotEmpty() {
		given()
				.get("/movies?title={title}", title)
				.then()
				.statusCode(200)
				// garante exatamente 1 item
				.body("content.size()", equalTo(1))

				// valida o único item
				.body("content[0].id", equalTo(1))
				.body("content[0].title", equalTo("The Witcher"))
				.body("content[0].score", equalTo(4.5f))
				.body("content[0].count", equalTo(2))
				.body("content[0].image", containsString("themoviedb.org"));

	}
	
	@Test
	public void findByIdShouldReturnMovieWhenIdExists() {
		given()
				.header("Content-type", "application/json")
				.accept(ContentType.JSON)
				.when()
				.get("/movies/{id}", existindMovieId)
				.then()
				.statusCode(200)
				.body("id", equalTo(1))
				.body("title", equalTo("The Witcher"))
				.body("score", equalTo(4.5f))
				.body("count", equalTo(2))
				.body("image", containsString("themoviedb.org"));
	}

	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() {
		given()
				.header("Content-type", "application/json")
				.accept(ContentType.JSON)
				.when()
				.get("/movies/{id}", nonExistindMovieId)
				.then()
				.statusCode(404);
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndBlankTitle() throws JSONException {
		item.put("title", "");
		JSONObject newOrder = new JSONObject(item);
		given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + clientToken)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(newOrder)
				.when()
				.post("/movies")
				.then()
				.statusCode(422);
	}
	
	@Test
	public void insertShouldReturnForbiddenWhenClientLogged() throws Exception {
		JSONObject newOrder = new JSONObject(item);
		given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + clientToken)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(newOrder)
				.when()
				.post("/movies")
				.then()
				.statusCode(403);

	}
	
	@Test
	public void insertShouldReturnUnauthorizedWhenInvalidToken() throws Exception {
		JSONObject newOrder = new JSONObject(item);
		given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + invalidToken)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(newOrder)
				.when()
				.post("/movies")
				.then()
				.statusCode(401);
	}
}
