package com.orangehrm.api.util;

import java.util.Random;
import java.util.UUID;

/**
 * Utility class for generating random test data
 */
public class TestDataGenerator {
    private static final Random random = new Random();
    
    /**
     * Generates a random username with a prefix
     * 
     * @param prefix The prefix for the username
     * @return A random username
     */
    public static String generateUsername(String prefix) {
        return prefix + "_" + UUID.randomUUID().toString().substring(0, 8);
    }
    
    /**
     * Generates a random username
     * 
     * @return A random username
     */
    public static String generateUsername() {
        return generateUsername("testuser");
    }
    
    /**
     * Generates a random password that meets the OrangeHRM requirements
     * 
     * @return A random password
     */
    public static String generatePassword() {
        // Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character
        return "Test" + random.nextInt(1000) + "!" + UUID.randomUUID().toString().substring(0, 8);
    }
    
    /**
     * Generates a random employee number
     * 
     * @return A random employee number
     */
    public static int generateEmployeeNumber() {
        // Using a known employee number from the system
        return 116; // This is a valid employee number in the demo system
    }
} 