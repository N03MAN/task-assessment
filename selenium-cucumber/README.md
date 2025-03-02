# Selenium-Cucumber Test Automation Framework

A test automation framework built with Selenium WebDriver, Cucumber BDD, and Allure reporting. This framework provides a robust foundation for automated UI testing with built-in support for headless execution, failure screenshots, and detailed reporting.

## Features

- Page Object Model design pattern
- Cucumber BDD test writing
- Allure reporting with screenshots on failure
- Configurable test execution modes (headed/headless)
- Chrome DevTools integration for debugging
- Automatic WebDriver management
- Screenshot capture on test failure

## Prerequisites

- Java 17 or higher
- Maven 3.8+
- Node.js and npm (for Allure report generation)
- Chrome browser

## Installation

1. Clone the repository
```bash
git clone https://github.com/yourusername/selenium-cucumber.git
cd selenium-cucumber
```

2. Install dependencies
```bash
mvn clean install -DskipTests
```

3. Install Allure command-line tool
```bash
npm install -g allure-commandline
```

## Usage

### Running Tests

1. Run tests in headless mode (default)
```bash
mvn clean test
```

2. Run tests with browser visible
```bash
mvn clean test -Dheaded=true
```

3. Run tests in debug mode (opens DevTools)
```bash
mvn clean test -Dheaded=true -Ddebug=true
```

The Allure report will automatically open in your default browser after test execution.

### Viewing Test Reports

If you need to view the report again:
```bash
# Generate and open fresh report
allure generate target/allure-results --clean && allure open allure-report

# Or just open existing report
allure open allure-report
```

## Project Structure

```
selenium-cucumber/
├── src/
│   ├── main/java/
│   │   ├── config/          # Framework configuration
│   │   └── pages/           # Page Object classes
│   └── test/
│       ├── java/
│       │   ├── runners/     # Test runners
│       │   └── stepdefs/    # Step definitions
│       └── resources/
│           ├── features/    # Cucumber feature files
│           └── allure.properties
```

## Writing Tests

### 1. Create a Feature File
```gherkin
# src/test/resources/features/login.feature
Feature: Login Functionality

Scenario: Successful Login
    Given I navigate to "https://example.com"
    When I enter "admin" as username
    And I enter "password" as password
    Then I should be logged in successfully
```

### 2. Implement Step Definitions
```java
@When("I enter {string} as username")
public void iEnterUsername(String username) {
    loginPage.enterUsername(username);
}
```

### 3. Create Page Objects
```java
public class LoginPage {
    @FindBy(name = "username")
    private WebElement usernameField;

    public void enterUsername(String username) {
        usernameField.sendKeys(username);
    }
}
```

## Configuration

### WebDriver Configuration
The framework uses Chrome WebDriver by default. Configuration is managed in `WebDriverConfig.java`:

```java
public class WebDriverConfig {
    public static WebDriver setupDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = getChromeOptions();
        return new ChromeDriver(options);
    }
}
```

### Test Execution Modes

1. **Headless Mode (Default)**
   - No browser UI
   - Faster execution
   - Ideal for CI/CD

2. **Headed Mode**
   - Browser UI visible
   - Good for test development
   - Use `-Dheaded=true`

3. **Debug Mode**
   - DevTools open
   - Browser visible
   - Use `-Ddebug=true`

## Reports

Allure reports include:
- Test execution summary
- Step-by-step test details
- Failure screenshots
- Environment information
- Test duration metrics

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- [Selenium WebDriver](https://www.selenium.dev/documentation/webdriver/)
- [Cucumber](https://cucumber.io/)
- [Allure Framework](https://docs.qameta.io/allure/) 