Feature: Testing add products REST API
  Users should be able to add products to cart

  Scenario: Add additional products of the same type to the shopping cart
    Given An empty shopping cart
    And A product, Dove Soap with a unit price of 39.99
    And Another product, Axe Deo with a unit price of 99.99
    And A sales tax rate of 12.5%
    When The user adds 2 Dove Soaps to the shopping cart
    And Then adds another 2 Axe Deos to the shopping cart
    Then The shopping cart should contain 2 Dove Soaps each with a unit price of 39.99
    And The shopping cart should contain 2 Axe Deos each with a unit price of 99.99
    And The total sales tax amount for the shopping cart should equal 35.00
    And The shopping cart’s total price should equal 314.96