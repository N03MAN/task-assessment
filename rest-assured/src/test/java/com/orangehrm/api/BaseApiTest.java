package com.orangehrm.api;

import com.orangehrm.api.config.ApiConfig;
import com.orangehrm.api.util.AuthenticationUtil;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

/**
 * Base class for API tests
 */
public class BaseApiTest {
    protected static final Logger logger = LoggerFactory.getLogger(BaseApiTest.class);
    protected RequestSpecification requestSpec;
    protected String sessionCookie;
    protected String csrfToken;
    
    @BeforeClass
    public void setupClass() {
        // Configure REST Assured
        RestAssured.baseURI = ApiConfig.BASE_URL;
        
        // Authenticate and get session cookie and CSRF token
        AuthenticationUtil.AuthResult authResult = AuthenticationUtil.authenticateWithDefaultCredentials();
        sessionCookie = authResult.getSessionCookie();
        csrfToken = authResult.getCsrfToken();
        
        logger.info("Session cookie obtained: {}", sessionCookie);
        logger.info("CSRF token obtained: {}", csrfToken);
        
        // Create base request specification
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addCookie("orangehrm", sessionCookie)
                .addHeader("User-Agent", ApiConfig.USER_AGENT)
                .addHeader("Origin", ApiConfig.BASE_URL)
                .addHeader("Referer", ApiConfig.BASE_URL + ApiConfig.DASHBOARD_ENDPOINT)
                .addFilter(new AllureRestAssured())
                .log(LogDetail.ALL);
        
        // Add CSRF token if available
        if (csrfToken != null && !csrfToken.isEmpty()) {
            requestSpecBuilder.addHeader("X-CSRF-Token", csrfToken);
        }
        
        requestSpec = requestSpecBuilder.build();
    }
    
    @BeforeMethod
    public void setupMethod() {
        logger.info("Starting test method");
    }
} 