package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestDataGenerator {
    
    public static String generateUniqueUsername(String baseUsername) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddHHmmss"));
        return baseUsername + timestamp;
    }
    
    public static String generateEmployeeName(String baseName) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddHHmm"));
        return baseName + " " + timestamp;
    }
    
    public static String generateStrongPassword() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("mmss"));
        return "StrongPass" + timestamp + "123";
    }
} 