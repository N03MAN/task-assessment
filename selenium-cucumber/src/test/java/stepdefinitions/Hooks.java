package stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {
    private TestContext context;
    
    public Hooks() {
        context = TestContext.getInstance();
    }
    
    @Before
    public void setUp(Scenario scenario) {
        context.setUp(scenario);
    }
    
    @After
    public void tearDown(Scenario scenario) {
        context.tearDown(scenario);
    }
} 