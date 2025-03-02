package config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public class WebDriverConfig {
    
    public static WebDriver setupDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = getChromeOptions();
        WebDriver driver = new ChromeDriver(options);
        configureDriverTimeouts(driver);
        return driver;
    }

    private static ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        
        // Check if headless mode is disabled via system property
        boolean isHeaded = Boolean.getBoolean("headed");
        
        if (!isHeaded) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
        } else {
            options.addArguments("--start-maximized");
        }
        
        // Add debug options if debug mode is enabled
        if (Boolean.getBoolean("debug")) {
            options.addArguments("--auto-open-devtools-for-tabs");
        }
        
        options.addArguments("--remote-allow-origins=*");
        return options;
    }

    private static void configureDriverTimeouts(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
    }
} 