Feature: Basic System Administration Tests

  @user-management
  Scenario: Administrator creates a new user in the system
    # Step 1: Login to the system
    Given the administrator is on the login page "https://opensource-demo.orangehrmlive.com/"
    When the administrator provides username "Admin"
    And the administrator provides password "admin123"
    And the administrator submits login credentials
    Then the administrator should have access to the system

    # Step 2: Navigate to User Management
    When the administrator navigates to User Management
    Then the system should display total number of users

    # Step 3: Create a new user
    When the administrator initiates new user creation
    Then the Add User form should be displayed
    When the administrator creates a new user with role "ESS" and base name "TestUser"
    And the administrator saves the new user details
    Then the system should confirm the user creation 