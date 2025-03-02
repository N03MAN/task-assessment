package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;
    
    private By usernameField = By.name("username");
    private By passwordField = By.name("password");
    private By loginButton = By.cssSelector("button[type='submit']");
    private By dashboardHeader = By.cssSelector(".oxd-text.oxd-text--h6.oxd-topbar-header-breadcrumb-module");
    private By errorMessage = By.cssSelector(".oxd-alert-content-text");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Step("Navigating to {url}")
    public void navigateTo(String url) {
        try {
            driver.get(url);
            // Wait for the login form to be visible
            wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
        } catch (TimeoutException e) {
            throw new RuntimeException("Login page did not load within timeout period", e);
        }
    }

    @Step("Entering username: {username}")
    public void enterUsername(String username) {
        try {
            WebElement usernameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
            usernameElement.clear(); // Clear any existing text
            usernameElement.sendKeys(username);
        } catch (TimeoutException e) {
            throw new RuntimeException("Username field not accessible within timeout period", e);
        }
    }

    @Step("Entering password")
    public void enterPassword(String password) {
        try {
            WebElement passwordElement = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
            passwordElement.clear(); // Clear any existing text
            passwordElement.sendKeys(password);
        } catch (TimeoutException e) {
            throw new RuntimeException("Password field not accessible within timeout period", e);
        }
    }

    @Step("Clicking login button")
    public void clickLoginButton() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
        } catch (TimeoutException e) {
            throw new RuntimeException("Login button not clickable within timeout period", e);
        }
    }

    @Step("Verifying successful login")
    public boolean isLoggedInSuccessfully() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(dashboardHeader))
                      .isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    @Step("Getting error message")
    public String getErrorMessage() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage))
                      .getText();
        } catch (TimeoutException e) {
            return null;
        }
    }

    public boolean isOnLoginPage() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField))
                      .isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
}
