# OrangeHRM API Tests with REST Assured

This project contains API tests for the OrangeHRM system using REST Assured.

## Project Structure

```
rest-assured/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── orangehrm/
│   │               └── api/
│   │                   ├── config/
│   │                   │   └── ApiConfig.java
│   │                   ├── model/
│   │                   │   ├── User.java
│   │                   │   └── DeleteUserRequest.java
│   │                   └── util/
│   │                       ├── AuthenticationUtil.java
│   │                       └── TestDataGenerator.java
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── orangehrm/
│       │           └── api/
│       │               ├── BaseApiTest.java
│       │               └── UserManagementApiTest.java
│       └── resources/
│           └── testng.xml
└── pom.xml
```

## Features

- Create a new user via API
- Delete a user via API
- Authentication with the OrangeHRM system
- Random test data generation
- Allure reporting

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

## Running the Tests

To run the tests, use the following command:

```bash
mvn clean test
```

To generate the Allure report:

```bash
mvn allure:report
```

To view the Allure report:

```bash
mvn allure:serve
```

## Test Cases

### User Management API

1. **Create User**
   - Creates a new user with random username and password
   - Verifies the user was created successfully

2. **Delete User**
   - Deletes the user created in the previous test
   - Verifies the user was deleted successfully

## Notes

- The tests use the demo OrangeHRM system at https://opensource-demo.orangehrmlive.com/
- The tests authenticate with the default admin credentials
- The employee number used in the tests is a known valid employee in the demo system 