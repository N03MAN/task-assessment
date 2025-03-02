package stepdefinitions;

import io.cucumber.java.en.*;
import io.qameta.allure.Allure;
import static org.junit.Assert.assertTrue;

public class LoginSteps {
    private TestContext context;
    
    public LoginSteps() {
        context = TestContext.getInstance();
    }

    @Given("the administrator is on the login page {string}")
    public void administratorIsOnLoginPage(String url) {
        context.loginPage.navigateTo(url);
    }

    @When("the administrator provides username {string}")
    public void administratorProvidesUsername(String username) {
        context.loginPage.enterUsername(username);
    }

    @When("the administrator provides password {string}")
    public void administratorProvidesPassword(String password) {
        context.loginPage.enterPassword(password);
    }

    @When("the administrator submits login credentials")
    public void administratorSubmitsLoginCredentials() {
        context.loginPage.clickLoginButton();
    }

    @Then("the administrator should have access to the system")
    public void administratorShouldHaveSystemAccess() {
        assertTrue("Administrator access verification failed", context.loginPage.isLoggedInSuccessfully());
    }
} 