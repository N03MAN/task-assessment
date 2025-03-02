# Cypress User Management Tests

This project contains Cypress tests for the user management functionality of the OrangeHRM demo application.

## Prerequisites

- Node.js (v14 or higher)
- npm (v6 or higher)

## Installation

1. Clone this repository
2. Navigate to the project directory
3. Install dependencies:

```bash
npm install
```

## Running Tests

To open Cypress Test Runner:

```bash
npm run cypress:open
```

To run tests in headless mode:

```bash
npm run cypress:run
```

## Test Scenarios

- User Management
  - Administrator creates a new user in the system

## Project Structure

- `cypress/e2e/` - Test files
- `cypress/support/` - Support files (commands, utilities)
- `cypress/fixtures/` - Test data

## Key Features

- Custom commands for common actions
- Screenshot capture at key points in the test
- Error checking and logging
- Record count verification before and after user creation
- Random username generation to avoid conflicts 