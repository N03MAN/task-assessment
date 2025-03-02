package stepdefinitions;

import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pages.LoginPage;
import pages.MenuPage;
import pages.AdminPage;
import pages.AddUserPage;
import config.WebDriverConfig;

import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * Shared context between step definition classes.
 * This is NOT a test class and should not be treated as such by the test runner.
 * It is a utility class for sharing state between step definition classes.
 */
public class TestContext {
    private static TestContext instance;
    
    public WebDriver driver;
    public LoginPage loginPage;
    public MenuPage menuPage;
    public AdminPage adminPage;
    public AddUserPage addUserPage;
    public String currentScenarioName;
    public String currentUsername;
    public String selectedEmployeeName;
    public int initialRecordCount;
    
    private TestContext() {
        // Private constructor to enforce singleton pattern
    }
    
    public static synchronized TestContext getInstance() {
        if (instance == null) {
            instance = new TestContext();
        }
        return instance;
    }
    
    public void setUp(Scenario scenario) {
        Allure.epic("System Administration");
        Allure.feature(scenario.getName());
        driver = WebDriverConfig.setupDriver();
        loginPage = new LoginPage(driver);
        menuPage = new MenuPage(driver);
        adminPage = new AdminPage(driver);
        addUserPage = new AddUserPage(driver);
        currentScenarioName = scenario.getName();
    }
    
    public void tearDown(Scenario scenario) {
        try {
            if (scenario.isFailed() && driver != null) {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment("System State at Failure", "image/png", new ByteArrayInputStream(screenshot), "png");
            }
        } finally {
            if (driver != null) {
                driver.quit();
            }
            // Reset the instance for the next test run
            instance = null;
        }
    }
    
    /**
     * Helper method to check for error messages on the page
     */
    public void checkForErrorMessages(String stage) {
        try {
            // Look for error messages
            List<WebElement> errorMessages = 
                driver.findElements(By.cssSelector(".oxd-input-field-error-message"));
            
            if (!errorMessages.isEmpty()) {
                StringBuilder errors = new StringBuilder();
                errors.append("Found ").append(errorMessages.size()).append(" error messages:\n");
                
                for (WebElement error : errorMessages) {
                    errors.append("- ").append(error.getText()).append("\n");
                }
                
                Allure.addAttachment("Error Messages at " + stage, errors.toString());
                
                // Take a screenshot showing the errors
                if (driver instanceof TakesScreenshot) {
                    byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                    Allure.addAttachment("Errors Screenshot at " + stage, "image/png", 
                                        new ByteArrayInputStream(screenshot), "png");
                }
            } else {
                Allure.addAttachment("Error Check at " + stage, "No error messages found");
            }
        } catch (Exception e) {
            Allure.addAttachment("Error Check Exception at " + stage, "Error: " + e.getMessage());
        }
    }
} 