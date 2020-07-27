package com.vikas.cart.junit.controllersImpl;

import com.vikas.cart.controllersImpl.CartController;
import com.vikas.cart.model.Cart;
import com.vikas.cart.services.CartService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerUnitTest {
    @Autowired
    private CartController cartController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Before
    public void setUp() {
    }

    @Test
    public void contextLoads() {
        assertThat(cartController).isNotNull();
    }

    @Test
    public void createCartTest() throws Exception {
        MvcResult mockResponse = this.mockMvc.perform(post("/cart")).
                andExpect(status().isOk()).andReturn();

        String response = mockResponse.getResponse().getContentAsString();
        assertThat(response).contains("\"cartPriceWithTax\":0.0");
        assertThat(response).contains("\"totalNumberOfItemsInCart\":0");
    }

    @Test
    public void getCartTest() throws Exception {
        when(cartService.getCart("1234")).thenReturn(new Cart());

        MvcResult mockResponse = this.mockMvc.perform(get("/cart/1234")).
                andExpect(status().isOk()).andReturn();

        String response = mockResponse.getResponse().getContentAsString();
        assertThat(response).contains("\"cartPriceWithTax\":0.0");
        assertThat(response).contains("\"totalNumberOfItemsInCart\":0");
    }

    @Test
    public void updateCartTest() throws Exception {
        Cart responseCart = new Cart();
        responseCart.cartItems(new ArrayList<>());

        Cart updatedCart = new Cart();
        updatedCart.cartItems(new ArrayList<>());
        updatedCart.setCartPriceWithTax(199.95f);
        updatedCart.setTotalNumberOfItemsInCart(5);

        when(cartService.getCart("1234")).thenReturn(responseCart);
        when(cartService.processCart(eq("1234"), any())).thenReturn(updatedCart);

        MvcResult mockResponse = this.mockMvc.perform(patch("/cart/1234")
                .contentType("application/json-patch+json")
                .content("[\n" +
                        "    {\n" +
                        "        \"op\": \"add\",\n" +
                        "        \"path\": \"/cartItems/0\",\n" +
                        "        \"value\": {\n" +
                        "            \"product\": {\n" +
                        "                \"price\": 39.99,\n" +
                        "                \"name\": \"Dove Soap\"\n" +
                        "            },\n" +
                        "            \"quantity\": 5\n" +
                        "        }\n" +
                        "    }\n" +
                        "]")).
                andExpect(status().isOk()).andReturn();

        String response = mockResponse.getResponse().getContentAsString();
        assertThat(response).contains("\"cartPriceWithTax\":199.95");
        assertThat(response).contains("\"totalNumberOfItemsInCart\":5");
    }
}
