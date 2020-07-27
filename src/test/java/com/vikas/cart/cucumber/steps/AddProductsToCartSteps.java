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
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class AddProductsToCartSteps {

    JSONObject cart;
    JSONArray cartItems;
    JSONObject doveSoap;
    JSONObject axeDeo;

    Map<String, JSONObject> productsMap = new HashMap<>();

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
        doveSoap = new JSONObject("{\"name\": \"" + productName + "\",\"price\": " + price + " }");
        productsMap.put(productName, doveSoap);
    }

    @And("^Another product, (.*) with a unit price of (\\d+.\\d+)$")
    public void anotherProductAxeDeoWithAUnitPriceOf(String productName, float price) throws JSONException {
        axeDeo = new JSONObject("{\"name\": \"" + productName + "\",\"price\": " + price + " }");
        productsMap.put(productName, axeDeo);
    }

    @When("^The user adds (\\d+) (.*)s to the shopping cart$")
    public void theUserAddsAProductToTheShoppingCart(int quantity, String productName) throws JSONException, IOException {
        updateShoppingCart(cart.get("id").toString(),
                buildProductBody(quantity, productsMap.get(productName)).toString());
    }

    @And("^Then adds another (\\d+) (.*)s to the shopping cart$")
    public void thenAddsAnotherProductToTheShoppingCart(int quantity, String productName) throws JSONException, IOException {
        updateShoppingCart(cart.get("id").toString(),
                buildProductBody(quantity, productsMap.get(productName)).toString());
    }

    @Then("^The shopping cart should contain (\\d+) (.+)s each with a unit price of (\\d+\\.\\d+)$")
    public void theShoppingCartShouldContainDoveSoapsEachWithAUnitPriceOf(int quantity, String productName, double price) throws JSONException {
        JSONArray cartItems = (JSONArray) cart.get("cartItems");
        int toBeAssertedQuantity = 0;
        double toBeAssertedPrice = 0;

        for (int i = 0; i < cartItems.length(); i++) {
            JSONObject item = (JSONObject) cartItems.get(i);
            JSONObject product = (JSONObject) item.get("product");

            if (product.get("name").equals(productName)) {
                toBeAssertedQuantity = toBeAssertedQuantity + (int) item.get("quantity");
                toBeAssertedPrice = (double) product.get("price");
            }
        }
        assertEquals(quantity, toBeAssertedQuantity);
        assertEquals(price, toBeAssertedPrice, 1e-15);
    }

    @And("^The shopping cart’s total price should equal (\\d+\\.\\d+)$")
    public void theShoppingCartSTotalPriceShouldEqual(double totalPrice) throws JSONException {
        assertEquals(totalPrice, cart.get("cartPriceWithTax"));
    }

    @And("^A sales tax rate of (\\d+.\\d+)%$")
    public void addTaxToCart(double tax) throws JSONException, IOException {
        updateShoppingCart(cart.get("id").toString(),
                buildSalesTaxBody(tax).toString());
    }

    @And("^The total sales tax amount for the shopping cart should equal (\\d+.\\d+)$")
    public void theTotalSalesTaxAmountForTheShoppingCartShouldEqual(double totalTaxAmount) throws JSONException {
        assertEquals(totalTaxAmount,
                (Double) cart.get("cartPriceWithTax") - (Double) cart.get("cartPriceWithoutTax"),
                1e-15);
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

    private JSONArray buildProductBody(int quantity, JSONObject product) throws JSONException {
        JSONObject productValue = new JSONObject();
        productValue.put("quantity", quantity);
        productValue.put("product", product);

        JSONObject patchOperation = new JSONObject();
        patchOperation.put("op", "add");
        patchOperation.put("path", "/cartItems/0");
        patchOperation.put("value", productValue);

        JSONArray requestBodyArr = new JSONArray();
        requestBodyArr.put(patchOperation);
        return requestBodyArr;
    }

    private JSONArray buildSalesTaxBody(double tax) throws JSONException {
        JSONObject patchOperation = new JSONObject();
        patchOperation.put("op", "replace");
        patchOperation.put("path", "/salesTax");
        patchOperation.put("value", tax);

        JSONArray requestBodyArr = new JSONArray();
        requestBodyArr.put(patchOperation);
        return requestBodyArr;
    }
}