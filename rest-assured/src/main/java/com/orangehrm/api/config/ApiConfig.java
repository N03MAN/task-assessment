package com.orangehrm.api.config;

/**
 * Configuration for the OrangeHRM API
 */
public class ApiConfig {
    // Base URL for the API
    public static final String BASE_URL = "https://opensource-demo.orangehrmlive.com";
    
    // Authentication endpoints
    public static final String LOGIN_PAGE_ENDPOINT = "/web/index.php/auth/login";
    public static final String AUTH_VALIDATE_ENDPOINT = "/web/index.php/auth/validate";
    
    // API endpoints
    public static final String DASHBOARD_ENDPOINT = "/web/index.php/dashboard/index";
    public static final String USERS_ENDPOINT = "/web/index.php/api/v2/admin/users";
    public static final String CSRF_TOKEN_ENDPOINT = "/web/index.php/api/v2/core/csrf-token";
    
    // Default credentials
    public static final String DEFAULT_USERNAME = "Admin";
    public static final String DEFAULT_PASSWORD = "admin123";
    
    // User agent
    public static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/133.0.0.0 Safari/537.36";
} 