package com.devsuperior.dsmovie.controllers;

import com.devsuperior.dsmovie.tests.TokenUtil;
import io.restassured.http.ContentType;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class ScoreControllerRA {

	private String clientUsername, clientPassword, adminUsername, adminPassword, adminOnlyUsername, adminOnlyPassword;
	private String clientToken, adminToken, adminOnlyToken, invalidToken;

	private Long existindScoreId, nonExistindScoreId;
	private Map<String, Object> item = new HashMap<>();

	@BeforeEach
	public void setup() throws Exception {
		baseURI = "http://localhost:8080";
		existindScoreId = 1L;
		nonExistindScoreId = 100L;

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

		item.put("movieId", 1);
		item.put("score", 4);

	}
	
	@Test
	public void saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist() throws Exception {
		item.put("movieId", 100);
		JSONObject score = new JSONObject(item);
		given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + adminToken)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(score)
				.when()
				.put("/scores")
				.then()
				.statusCode(404)
				.body("error", equalTo("Recurso não encontrado"))
				.body("status", equalTo(404));
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId() throws Exception {

		item.remove("movieId");
		JSONObject score = new JSONObject(item);
		given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + adminToken)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(score)
				.when()
				.put("/scores")
				.then()
				.statusCode(422);
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero() throws Exception {
		item.put("score", -1);
		JSONObject score = new JSONObject(item);
		given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + adminToken)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(score)
				.when()
				.put("/scores")
				.then()
				.statusCode(422);
	}
}
