Feature: Testing add products REST API
  Users should be able to add products with offer to cart

  Scenario: 4. Add products to the shopping cart, which have “Buy X, Get Y free” offer
    Given An empty shopping cart
    And A product, Dove Soap with a unit price of 39.99 and a associated Buy 2 Get 1 Free offer
    And Another product, Axe Deo with a unit price of 89.99

    When The user adds 3 Dove Soaps to the shopping cart
    And A sales tax rate of 12.5%

    Then The shopping cart should contain 3 Dove Soaps each with a unit price of 39.99
    And The shopping cart’s total price should equal 89.98
    And The shopping cart’s total discount should equal 39.99
    And The total sales tax amount for the shopping cart should equal 10.00

    When The user adds 5 Dove Soaps to the shopping cart
    And A sales tax rate of 12.5%

    Then The shopping cart should contain 5 Dove Soaps each with a unit price of 39.99
    And The shopping cart’s total price should equal 179.96
    And The shopping cart’s total discount should equal 39.99
    And The total sales tax amount for the shopping cart should equal 20.00

    When The user adds 3 Dove Soaps to the shopping cart
    And A sales tax rate of 12.5%
    And Then adds another 2 Axe Deos to the shopping cart

    Then The shopping cart should contain 3 Dove Soaps each with a unit price of 39.99
    And The shopping cart should contain 2 Axe Deos each with a unit price of 89.99
    And The shopping cart’s total price should equal 292.96
    And The shopping cart’s total discount should equal 39.99
    And The total sales tax amount for the shopping cart should equal 33.0

