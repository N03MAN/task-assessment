package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class MenuPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(css = ".oxd-sidepanel-body")
    private WebElement sidePanel;

    @FindBy(css = "a.oxd-main-menu-item[href*='viewAdminModule']")
    private WebElement adminMenuItem;

    public MenuPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    @Step("Clicking on Admin menu item")
    public void clickAdminMenuItem() {
        wait.until(ExpectedConditions.visibilityOf(sidePanel));
        wait.until(ExpectedConditions.elementToBeClickable(adminMenuItem)).click();
    }
} 