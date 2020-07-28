Feature: Testing add products REST API
  Users should be able to add products to cart with sales tax

  Scenario: 3. Calculate the tax rate of the shopping cart with multiple items
    Given An empty shopping cart
    And A product, Dove Soap with a unit price of 39.99
    And Another product, Axe Deo with a unit price of 89.99
    And A global 20% discount if the cart total is greater than or equal to 500

    When The user adds 5 Dove Soaps to the shopping cart
    And A sales tax rate of 12.5%
    And Then adds another 4 Axe Deos to the shopping cart

    Then The shopping cart should contain 5 Dove Soaps each with a unit price of 39.99
    And The shopping cart should contain 4 Axe Deos each with a unit price of 89.99
    And The shopping cart’s total price should equal 503.93
    And The shopping cart’s total discount should equal 111.98
    And The total sales tax amount for the shopping cart should equal 56.00