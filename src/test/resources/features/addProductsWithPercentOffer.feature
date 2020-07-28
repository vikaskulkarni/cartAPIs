Feature: Testing add products REST API
  Users should be able to add products to cart with offer

  Scenario: 5. Add products to the Shopping Cart, which have “Buy 1, Get 50% discount on next one” offer
    Given An empty shopping cart
    And A product, Dove Soap with a unit price of 39.99 and a associated Buy 1 Get 50% Free offer
    When The user adds 2 Dove Soaps to the shopping cart
    Then The shopping cart should contain 2 Dove Soaps each with a unit price of 39.99
    And A sales tax rate of 12.5%
    And The shopping cart’s total price should equal 67.98
    And The shopping cart’s total discount should equal 20.00
    And The total sales tax amount for the shopping cart should equal 8.0