Scenario: Navigation

    Given I'm on the page x
    When I click on y
    Then I'm redirected to z page

Scenario: Login - Success

    Given I'm on login page
    When I enter the username: "Bob the Builder"
    And the password: "password123"
    Then I'm redirected to dashboard page
    And have "Bob the Builder" present in the navbar

Scenario: Login - Fail

    Given I'm on login page
    When I enter the username: "Bob the Builder"
    And the password: "fakepass"
    Then I should check error message: "username/password incorrect"

Scenario: Register

    Given I'm on Registration Page
    When I enter username: "Bob the Builder"
    And the password: "password123"
    Then I'm redirected to dashboard page
    And have "Bob the Builder" present in the navbar

Scenario: Add to Cart

    Given I'm on Product Page
    When I click on the button "add to cart"
    Then I should see an alert with "item added"

Scenario: Add More than 1 Item to Cart - Success

    Given I'm on Product Page
    When I write 3 on the number of items
    And I click on the button "add to cart"
    Then I should see an alert with "item added" 

Scenario: Add More than 1 Item to Cart - Fail

    Given I'm on Product Page
    When I write 100 on the number of items
    And I click on the button "add to cart"
    Then I should see an alert with "Item out of Stock"

Scenario: Chekout Cart

    Given I'm on Cart Page
    When I click on the button "checkout"
    Then I should be redirected to Orders Page
    And should have an order with the status "Processing"

Scenario: Remove Item from Cart

    Given I'm on Cart Page
    When I click on the "remove item" button
    Then the item should dissapear
    
