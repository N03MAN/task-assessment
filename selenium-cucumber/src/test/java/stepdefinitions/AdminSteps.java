package stepdefinitions;

import io.cucumber.java.en.*;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;

public class AdminSteps {
    private TestContext context;
    
    public AdminSteps() {
        context = TestContext.getInstance();
    }
    
    @When("the administrator navigates to User Management")
    public void theAdministratorNavigatesToUserManagement() {
        context.menuPage.clickAdminMenuItem();
        
        // Verify we're on the Admin page
        assertTrue("Failed to navigate to Admin page", context.adminPage.isOnAdminPage());
    }
    
    @Then("the system should display total number of users")
    public void systemDisplaysTotalUsers() {
        // Store the initial record count for later verification
        context.initialRecordCount = context.adminPage.getRecordsCountAsInt();
        Allure.addAttachment("Initial Record Count", "Count: " + context.initialRecordCount);
        
        // Take a screenshot of the admin page
        if (context.driver instanceof TakesScreenshot) {
            byte[] screenshot = ((TakesScreenshot) context.driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Admin Page", "image/png", new ByteArrayInputStream(screenshot), "png");
        }
        
        // Verify we can see the records count
        String recordsText = context.adminPage.getRecordsCount();
        Allure.addAttachment("Records Text", recordsText);
        
        assertTrue("Records count should be visible", recordsText != null && !recordsText.isEmpty());
    }
    
    @When("the administrator initiates new user creation")
    public void theAdministratorInitiatesNewUserCreation() {
        context.adminPage.clickAddButton();
    }
} 