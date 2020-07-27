package com.vikas.cart.cucumber.steps;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class AddProductsToCartSteps {

    JSONObject cart;
    JSONArray cartItems;
    JSONObject product;

    CloseableHttpClient httpClient;

    @Before
    public void startUp() {
        cartItems = new JSONArray();
        httpClient = HttpClients.createDefault();
    }

    @Given("^An empty shopping cart$")
    public void createAnEmptyShoppingCart() throws IOException, JSONException {
        StringEntity entity = new StringEntity("{}");
        createShoppingCart(entity);
    }

    @Then("^Return an empty cart$")
    public void returnAnEmptyCart() {

    }

    @And("^A product, (.*) with a unit price of (\\d+\\.\\d+)$")
    public void andAProductDoveSoapWithAUnitPriceOf(String productName, float price) throws JSONException {
        product = new JSONObject("{\"name\": \"Dove Soap\",\"price\": 39.99 }");
    }

    @When("^The user adds (\\d+) (.*)s to the shopping cart$")
    public void theUserAddsDoveSoapsToTheShoppingCart(int quantity, String productName) throws JSONException, IOException {
        JSONObject productValue = new JSONObject();
        productValue.put("quantity", 5);
        productValue.put("product", product);

        JSONObject patchOperation = new JSONObject();
        patchOperation.put("op", "add");
        patchOperation.put("path", "/cartItems/0");
        patchOperation.put("value", productValue);

        JSONArray requestBodyArr = new JSONArray();
        requestBodyArr.put(patchOperation);

        updateShoppingCart(cart.get("id").toString(), requestBodyArr.toString());
    }

    @Then("^The shopping cart should contain (\\d+) (.+)s each with a unit price of (\\d+\\.\\d+)$")
    public void theShoppingCartShouldContainDoveSoapsEachWithAUnitPriceOf(int quantity, String productName, float price) throws JSONException {
        assertEquals(quantity, cart.get("totalNumberOfItemsInCart"));
    }

    @And("^the shopping cart’s total price should equal (\\d+\\.\\d+)$")
    public void theShoppingCartSTotalPriceShouldEqual(double totalPrice) throws JSONException {
        assertEquals(totalPrice, cart.get("cartPriceWithoutTax"));
    }

    @After
    public void cleanUp() {
    }


    private void createShoppingCart(StringEntity entity) throws IOException, JSONException {
        HttpPost request = new HttpPost("http://localhost:8081/cart/");
        request.addHeader("content-type", "application/json");
        request.setEntity(entity);
        HttpResponse response = httpClient.execute(request);
        cart = getContentFromResponse(response.getEntity());
    }

    private void updateShoppingCart(String id, String jsonBody) throws IOException, JSONException {
        StringEntity entity = new StringEntity(jsonBody);
        HttpPatch request = new HttpPatch("http://localhost:8081/cart/" + id);
        request.addHeader("content-type", "application/json-patch+json");
        request.setEntity(entity);
        HttpResponse response = httpClient.execute(request);
        cart = getContentFromResponse(response.getEntity());
    }

    private JSONObject getContentFromResponse(HttpEntity entity) throws IOException, JSONException {
        InputStream is = entity.getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.ISO_8859_1), 8);
        StringBuilder sb = new StringBuilder();

        String resString;
        try (Stream<String> stream = reader.lines()) {
            stream.forEach(line -> sb.append(line).append("\n"));
            resString = sb.toString();
        }

        is.close();

        return new JSONObject(resString);
    }


}