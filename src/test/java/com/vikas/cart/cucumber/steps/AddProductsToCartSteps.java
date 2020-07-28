package com.vikas.cart.cucumber.steps;

import com.vikas.cart.model.CartItem;
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
import java.math.BigDecimal;
import java.math.RoundingMode;
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
    Map<String, Integer> productsOfferCode = new HashMap<>();

    CloseableHttpClient httpClient;

    @Before
    public void startUp() {
        cartItems = new JSONArray();
        httpClient = HttpClients.createDefault();
    }

    @Given("^An empty shopping cart$")
    public void createAnEmptyShoppingCart() throws IOException, JSONException {
//        StringEntity entity = new StringEntity("{}");
//        createShoppingCart(entity);
    }

    @Then("^Return an empty cart$")
    public void returnAnEmptyCart() {

    }

    @And("^A product, (.*) with a unit price of (\\d+\\.\\d+)$")
    public void andAProductDoveSoapWithAUnitPriceOf(String productName, double price) throws JSONException {
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
        StringEntity entity = new StringEntity("{}");
        cart = createShoppingCart(entity);

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
        BigDecimal ta = new BigDecimal(
                (Double) cart.get("cartPriceWithTax") - (Double) cart.get("cartPriceWithoutTax")).
                setScale(2, RoundingMode.HALF_UP);
        assertEquals(totalTaxAmount,
                ta.floatValue(),
                1e-15);
    }

    @After
    public void cleanUp() {
    }


    private JSONObject createShoppingCart(StringEntity entity) throws IOException, JSONException {
        HttpPost request = new HttpPost("http://localhost:8081/cart/");
        request.addHeader("content-type", "application/json");
        request.setEntity(entity);
        HttpResponse response = httpClient.execute(request);
        return getContentFromResponse(response.getEntity());
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
        String productName = product.get("name").toString();
        int offerCode = productsOfferCode.get(productName) == null ? 0 : productsOfferCode.get(productName);

        JSONObject productValue = new JSONObject();
        productValue.put("quantity", quantity);
        productValue.put("product", product);
        productValue.put("offerCode", offerCode);

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

    @And("^A product, (.*) with a unit price of (\\d+.\\d+) and a associated Buy (\\d+) Get (\\d+)(.*) Free offer$")
    public void aProductDoveSoapWithAUnitPriceOfAndAAssociatedBuyGetFreeOffer(String productName,
                                                                              double price,
                                                                              int buyCount,
                                                                              int getCount, String criteria)
            throws JSONException {
        andAProductDoveSoapWithAUnitPriceOf(productName, price);
        int offerCode = 0;
        if (buyCount == 2 && getCount == 1)
            offerCode = 2;
        if (getCount == 50 && criteria.equals("%"))
            offerCode = 3;

        productsOfferCode.put(productName, offerCode);
    }

    @And("^The shopping cart’s total discount should equal (\\d+.\\d+)$")
    public void theShoppingCartSTotalDiscountShouldEqual(double totalDiscount) throws JSONException {

        double totalCartAmount = 0;
        int offerCode = 0;
        double productPrice = 0;
        int quantity = 0;
        JSONArray items = (JSONArray) cart.get("cartItems");

        for (int i = 0; i < items.length(); i++) {
            JSONObject obj = (JSONObject) items.get(i);
            offerCode = (int) obj.get("offerCode");
            productPrice = (double) ((JSONObject) obj.get("product")).get("price");
            quantity = (int) obj.get("quantity");
            double price = productPrice * quantity;

            totalCartAmount += price;
        }

        BigDecimal da = new BigDecimal(totalCartAmount).setScale(2, RoundingMode.HALF_UP);
        totalDiscount = (double) da.floatValue() - (double) cart.get("cartPriceWithoutTax");

        BigDecimal td = new BigDecimal(totalDiscount).setScale(2, RoundingMode.HALF_UP);

        System.out.println("offercode:" + offerCode + ", productPrice: " + productPrice + ", td.floatValue: " + td.floatValue());
        if (offerCode == 2)
            assert productPrice == td.doubleValue();
        if (offerCode == 3) {
            int totalItemTobeDiscounted = quantity / 2;
            double totalDiscountVal = totalItemTobeDiscounted * (Math.ceil(0.5 * productPrice));
            assert totalDiscountVal == td.doubleValue();
        }
    }
}