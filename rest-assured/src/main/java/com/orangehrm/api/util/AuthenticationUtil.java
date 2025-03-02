package com.orangehrm.api.util;

import com.orangehrm.api.config.ApiConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for handling authentication
 */
public class AuthenticationUtil {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationUtil.class);
    
    /**
     * Authentication result containing session cookie and CSRF token
     */
    public static class AuthResult {
        private final String sessionCookie;
        private final String csrfToken;
        
        public AuthResult(String sessionCookie, String csrfToken) {
            this.sessionCookie = sessionCookie;
            this.csrfToken = csrfToken;
        }
        
        public String getSessionCookie() {
            return sessionCookie;
        }
        
        public String getCsrfToken() {
            return csrfToken;
        }
    }
    
    /**
     * Authenticates with the OrangeHRM API and returns the session cookie and CSRF token
     * 
     * @param username The username to authenticate with
     * @param password The password to authenticate with
     * @return The authentication result containing session cookie and CSRF token
     */
    public static AuthResult authenticate(String username, String password) {
        logger.info("Authenticating with username: {}", username);
        
        // Enable logging for debugging
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        
        // First, get the login page to get the initial cookies and CSRF token
        Response loginPageResponse = RestAssured.given()
                .baseUri(ApiConfig.BASE_URL)
                .header("User-Agent", ApiConfig.USER_AGENT)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                .header("Accept-Language", "en-US,en;q=0.9")
                .redirects().follow(true)
                .get(ApiConfig.LOGIN_PAGE_ENDPOINT);
        
        if (loginPageResponse.getStatusCode() != 200) {
            logger.error("Failed to access login page: {}", loginPageResponse.getStatusCode());
            throw new RuntimeException("Failed to access login page: " + loginPageResponse.getStatusCode());
        }
        
        // Get cookies from the login page response
        Map<String, String> cookies = new HashMap<>(loginPageResponse.getCookies());
        String orangeHrmCookie = cookies.get("orangehrm");
        
        if (orangeHrmCookie == null || orangeHrmCookie.isEmpty()) {
            logger.error("No orangehrm cookie found in login page response");
            throw new RuntimeException("No orangehrm cookie found in login page response");
        }
        
        logger.info("Got initial cookie: {}", orangeHrmCookie);
        
        // Extract CSRF token from the login page
        String loginPageHtml = loginPageResponse.getBody().asString();
        String csrfToken = extractCsrfToken(loginPageHtml);
        
        if (csrfToken == null || csrfToken.isEmpty()) {
            logger.error("No CSRF token found in login page");
            throw new RuntimeException("No CSRF token found in login page");
        }
        
        logger.info("Got CSRF token from login page: {}", csrfToken);
        
        // Now perform the login with form parameters to the validate endpoint
        Response loginResponse = RestAssured.given()
                .baseUri(ApiConfig.BASE_URL)
                .header("User-Agent", ApiConfig.USER_AGENT)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                .header("Accept-Language", "en-US,en;q=0.9")
                .header("Cache-Control", "max-age=0")
                .header("Origin", ApiConfig.BASE_URL)
                .header("Referer", ApiConfig.BASE_URL + ApiConfig.LOGIN_PAGE_ENDPOINT)
                .header("Sec-Fetch-Dest", "document")
                .header("Sec-Fetch-Mode", "navigate")
                .header("Sec-Fetch-Site", "same-origin")
                .contentType("application/x-www-form-urlencoded")
                .cookie("orangehrm", orangeHrmCookie)
                .formParam("_token", csrfToken)
                .formParam("username", username)
                .formParam("password", password)
                .redirects().follow(false) // Don't follow redirects automatically
                .log().all()
                .post(ApiConfig.AUTH_VALIDATE_ENDPOINT);
        
        // Check response status
        int statusCode = loginResponse.getStatusCode();
        logger.info("Login response status code: {}", statusCode);
        
        // For a successful login, we should get a 302 redirect
        if (statusCode != 302) {
            logger.error("Authentication failed with status code: {}", statusCode);
            throw new RuntimeException("Authentication failed: " + statusCode);
        }
        
        // Get all cookies from the login response
        cookies.putAll(new HashMap<>(loginResponse.getCookies()));
        
        // Get the redirect location
        String redirectLocation = loginResponse.getHeader("Location");
        logger.info("Following redirect to: {}", redirectLocation);
        
        // Follow the redirect
        Response redirectResponse = RestAssured.given()
                .baseUri(ApiConfig.BASE_URL)
                .header("User-Agent", ApiConfig.USER_AGENT)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                .header("Accept-Language", "en-US,en;q=0.9")
                .cookies(cookies)
                .get(redirectLocation);
        
        if (redirectResponse.getStatusCode() != 200) {
            logger.error("Failed to follow redirect: {}", redirectResponse.getStatusCode());
            throw new RuntimeException("Failed to follow redirect: " + redirectResponse.getStatusCode());
        }
        
        // Update cookies with any new ones from the redirect
        cookies.putAll(new HashMap<>(redirectResponse.getCookies()));
        
        // Extract the session cookie
        orangeHrmCookie = cookies.get("orangehrm");
        
        if (orangeHrmCookie == null || orangeHrmCookie.isEmpty()) {
            logger.error("No session cookie found in response");
            throw new RuntimeException("No session cookie found in response");
        }
        
        // Extract the CSRF token from the dashboard page
        String dashboardHtml = redirectResponse.getBody().asString();
        String dashboardCsrfToken = extractCsrfToken(dashboardHtml);
        
        if (dashboardCsrfToken == null || dashboardCsrfToken.isEmpty()) {
            // Try to get it from a dedicated API endpoint
            Response tokenResponse = RestAssured.given()
                    .baseUri(ApiConfig.BASE_URL)
                    .header("User-Agent", ApiConfig.USER_AGENT)
                    .header("Accept", "application/json")
                    .cookies(cookies)
                    .get(ApiConfig.CSRF_TOKEN_ENDPOINT);
            
            if (tokenResponse.getStatusCode() == 200) {
                dashboardCsrfToken = tokenResponse.jsonPath().getString("data");
                logger.info("Got CSRF token from API: {}", dashboardCsrfToken);
            } else {
                logger.warn("Could not get CSRF token from API, will proceed without it");
                dashboardCsrfToken = "";
            }
        } else {
            logger.info("Extracted CSRF token from dashboard page: {}", dashboardCsrfToken);
        }
        
        logger.info("Authentication successful, session cookie obtained");
        return new AuthResult(orangeHrmCookie, dashboardCsrfToken);
    }
    
    /**
     * Extracts the CSRF token from the HTML
     * 
     * @param html The HTML content
     * @return The CSRF token or null if not found
     */
    private static String extractCsrfToken(String html) {
        // First try to find the token in the Vue.js component attribute (login page)
        Pattern vuePattern = Pattern.compile(":token=\"&quot;([^&]+)&quot;\"");
        Matcher vueMatcher = vuePattern.matcher(html);
        
        if (vueMatcher.find()) {
            return vueMatcher.group(1);
        }
        
        // If not found, try to find it in a meta tag (alternative login page format)
        Pattern metaPattern = Pattern.compile("<input type=\"hidden\" name=\"_token\" value=\"([^\"]+)\"");
        Matcher metaMatcher = metaPattern.matcher(html);
        
        if (metaMatcher.find()) {
            return metaMatcher.group(1);
        }
        
        // If not found, try to find it in the JavaScript (dashboard page)
        Pattern jsPattern = Pattern.compile("\"csrf-token\"\\s*:\\s*\"([^\"]+)\"");
        Matcher jsMatcher = jsPattern.matcher(html);
        
        if (jsMatcher.find()) {
            return jsMatcher.group(1);
        }
        
        return null;
    }
    
    /**
     * Authenticates with the default credentials
     * 
     * @return The authentication result containing session cookie and CSRF token
     */
    public static AuthResult authenticateWithDefaultCredentials() {
        return authenticate(ApiConfig.DEFAULT_USERNAME, ApiConfig.DEFAULT_PASSWORD);
    }
} 