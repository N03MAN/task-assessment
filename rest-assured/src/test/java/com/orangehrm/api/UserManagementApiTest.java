package com.orangehrm.api;

import com.orangehrm.api.config.ApiConfig;
import com.orangehrm.api.model.DeleteUserRequest;
import com.orangehrm.api.model.User;
import com.orangehrm.api.util.TestDataGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;

/**
 * Test class for User Management API
 */
@Feature("User Management API")
public class UserManagementApiTest extends BaseApiTest {
    
    private int createdUserId;
    
    @Test(priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test to create a new user via API")
    @Story("Create User")
    public void testCreateUser() {
        // Generate random test data
        String username = TestDataGenerator.generateUsername();
        String password = TestDataGenerator.generatePassword();
        int empNumber = TestDataGenerator.generateEmployeeNumber();
        
        // Create user object
        User user = new User(username, password, true, 1, empNumber);
        logger.info("Creating user: {}", user);
        
        // Log the CSRF token being used
        logger.info("Using CSRF token: {}", csrfToken);
        logger.info("Using session cookie: {}", sessionCookie);
        
        // Send request to create user
        Response response = RestAssured.given()
                .spec(requestSpec)
                .body(user)
                .log().all() // Log the full request
                .post(ApiConfig.USERS_ENDPOINT);
        
        // Log the response for debugging
        logger.info("Response status code: {}", response.getStatusCode());
        logger.info("Response body: {}", response.getBody().asString());
        logger.info("Response headers: {}", response.getHeaders());
        
        // Verify response
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");
        
        // Parse response
        JsonPath jsonPath = response.jsonPath();
        createdUserId = jsonPath.getInt("data.id");
        String createdUsername = jsonPath.getString("data.userName");
        
        // Verify user was created correctly
        Assert.assertTrue(createdUserId > 0, "User ID should be greater than 0");
        Assert.assertEquals(createdUsername, username, "Username in response should match the one we sent");
        
        logger.info("User created successfully with ID: {}", createdUserId);
    }
    
    @Test(priority = 2, dependsOnMethods = "testCreateUser")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test to delete a user via API")
    @Story("Delete User")
    public void testDeleteUser() {
        logger.info("Starting test method");
        logger.info("Deleting user with ID: " + createdUserId);
        
        // Create request body with user ID to delete
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("ids", Collections.singletonList(createdUserId));
        
        // Send DELETE request
        Response response = given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .delete(ApiConfig.USERS_ENDPOINT)
                .then()
                .extract()
                .response();
        
        logger.info("Response status code: " + response.getStatusCode());
        logger.info("Response body: " + response.getBody().asString());
        logger.info("Response headers: " + response.getHeaders());
        
        // Verify response
        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        
        // Extract response data
        JsonPath jsonPath = response.jsonPath();
        
        // Check if the deleted user ID is in the response data array
        List<String> deletedIds = jsonPath.getList("data", String.class);
        Assert.assertTrue(deletedIds.contains(String.valueOf(createdUserId)), 
                "Response should contain the deleted user ID: " + createdUserId);
    }
} 