package com.vikas.cart.cucumber;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features",
        glue = "classpath:com.vikas.cart.cucumber.steps",
        plugin = "pretty")
public class AddProductsToCartTest {

}