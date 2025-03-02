// Custom commands for the OrangeHRM application

// Login command
Cypress.Commands.add('login', (username, password) => {
  cy.visit('/')
  cy.get('input[name="username"]').type(username)
  cy.get('input[name="password"]').type(password)
  cy.get('button[type="submit"]').click()
  cy.get('.oxd-text.oxd-text--h6.oxd-topbar-header-breadcrumb-module')
    .should('be.visible')
    .and('contain', 'Dashboard')
})

// Navigate to Admin page
Cypress.Commands.add('navigateToAdmin', () => {
  cy.get('.oxd-main-menu-item').contains('Admin').click()
  cy.get('.oxd-text.oxd-text--h5').should('contain', 'System Users')
})

// Get records count as integer
Cypress.Commands.add('getRecordsCount', () => {
  return cy.get('.orangehrm-horizontal-padding .oxd-text--span')
    .invoke('text')
    .then(text => {
      const match = text.match(/(\d+)\s+Records?/)
      return match ? parseInt(match[1], 10) : 0
    })
})

// Check for error messages
Cypress.Commands.add('checkForErrors', () => {
  cy.get('body').then($body => {
    if ($body.find('.oxd-input-field-error-message').length > 0) {
      cy.log('Error messages found:')
      cy.get('.oxd-input-field-error-message').each(($el) => {
        cy.log(`- ${$el.text()}`)
      })
    }
  })
})

// Take screenshot with custom name
Cypress.Commands.add('takeScreenshot', (name) => {
  cy.screenshot(`${name}-${new Date().getTime()}`)
})

// Enter password in the first password field
Cypress.Commands.add('enterPassword', (password) => {
  cy.get('input[type="password"]').first()
    .should('be.visible')
    .clear()
    .type(password)
})

// Enter confirm password in the second password field
Cypress.Commands.add('enterConfirmPassword', (password) => {
  cy.get('input[type="password"]').eq(1)
    .should('be.visible')
    .clear()
    .type(password)
})

// Select employee from dropdown with improved handling
Cypress.Commands.add('selectEmployee', (searchTerm) => {
  // Always use "test" as the search term, matching the Selenium implementation
  const effectiveSearchTerm = "test";
  cy.log(`Using search term: ${effectiveSearchTerm}`)
  
  // Clear and type in the search term
  cy.get('.oxd-autocomplete-text-input input')
    .clear()
    .type(effectiveSearchTerm, { force: true })
    .should('have.value', effectiveSearchTerm)
  
  // Wait for the API response and dropdown to appear
  cy.intercept('GET', '**/api/v2/pim/employees**').as('employeeSearch')
  cy.wait('@employeeSearch', { timeout: 20000 })
  
  // Ensure dropdown is visible before attempting to click
  cy.get('.oxd-autocomplete-dropdown')
    .should('be.visible')
    .then($dropdown => {
      // Take a screenshot of the dropdown
      cy.takeScreenshot('employee-dropdown')
      
      // Log available options
      cy.get('.oxd-autocomplete-dropdown .oxd-autocomplete-option').then($options => {
        cy.log(`Found ${$options.length} employee options`)
        $options.each((index, option) => {
          cy.log(`Option ${index + 1}: ${Cypress.$(option).text()}`)
        })
      })
      
      // Wait a moment to ensure all options are loaded
      cy.wait(2000)
      
      // Get all options and click the first one without scrolling
      cy.get('.oxd-autocomplete-dropdown .oxd-autocomplete-option')
        .should('have.length.at.least', 1)
        .first()
        .click({ force: true })
      
      // Wait for dropdown to disappear
      cy.get('.oxd-autocomplete-dropdown').should('not.exist')
      
      // Immediately check for error messages after selection
      cy.get('body').then($body => {
        if ($body.find('.oxd-input-field-error-message').length > 0) {
          cy.log('Error detected after employee selection:')
          cy.get('.oxd-input-field-error-message').each(($el) => {
            cy.log(`- ${$el.text()}`)
          })
          
          // If there's an error, try a fallback approach
          cy.log('Attempting fallback selection with "Orange Test"')
          cy.get('.oxd-autocomplete-text-input input')
            .clear()
            .type('Orange Test', { force: true })
          
          // Wait for the API response again
          cy.wait('@employeeSearch', { timeout: 10000 })
          
          // Try to select the first option again without scrolling
          cy.get('.oxd-autocomplete-dropdown')
            .should('be.visible')
            .then(() => {
              cy.get('.oxd-autocomplete-dropdown .oxd-autocomplete-option')
                .first()
                .click({ force: true })
            })
        } else {
          cy.log('No errors detected after employee selection')
        }
      })
    })
})

// Enter username with a more specific selector
Cypress.Commands.add('enterUsername', (username) => {
  // Use a more specific selector for the username field
  cy.get('.oxd-grid-item:nth-child(4) input[autocomplete="off"]')
    .should('be.visible')
    .clear()
    .type(username)
}) 