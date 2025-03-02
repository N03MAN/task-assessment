package pages;

import io.qameta.allure.Step;
import io.qameta.allure.Allure;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdminPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(css = ".oxd-topbar-header-breadcrumb-module")
    private WebElement pageHeader;

    @FindBy(css = ".oxd-text.oxd-text--span")
    private WebElement recordsCount;

    @FindBy(css = "button.oxd-button--secondary i.bi-plus")
    private WebElement addButton;

    public AdminPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    @Step("Getting records count from Admin page")
    public String getRecordsCount() {
        wait.until(ExpectedConditions.visibilityOf(pageHeader));
        try {
            return wait.until(ExpectedConditions.visibilityOf(recordsCount)).getText();
        } catch (Exception e) {
            Allure.addAttachment("Records Count Error", "Error getting records count: " + e.getMessage());
            return "0 Records Found"; // Default value if element not found
        }
    }
    
    @Step("Getting records count as integer")
    public int getRecordsCountAsInt() {
        String countText = getRecordsCount();
        Allure.addAttachment("Records Count Text", countText);
        
        // Extract the number from text like "(7) Records Found"
        Pattern pattern = Pattern.compile("\\(?(\\d+)\\)?");
        Matcher matcher = pattern.matcher(countText);
        
        if (matcher.find()) {
            String countStr = matcher.group(1); // Get the captured group (the number)
            try {
                int count = Integer.parseInt(countStr);
                Allure.addAttachment("Extracted Count", String.valueOf(count));
                return count;
            } catch (NumberFormatException e) {
                Allure.addAttachment("Count Parsing Error", "Error parsing count: " + e.getMessage());
                return 0;
            }
        } else {
            Allure.addAttachment("Count Extraction Failed", "No numbers found in: " + countText);
            return 0;
        }
    }

    @Step("Clicking Add button on Admin page")
    public void clickAddButton() {
        wait.until(ExpectedConditions.elementToBeClickable(addButton)).click();
    }

    @Step("Verifying if on Admin page")
    public boolean isOnAdminPage() {
        try {
            wait.until(ExpectedConditions.visibilityOf(pageHeader));
            String headerText = pageHeader.getText().trim().toLowerCase();
            return headerText.contains("admin") || headerText.contains("user management");
        } catch (Exception e) {
            return false;
        }
    }
} 