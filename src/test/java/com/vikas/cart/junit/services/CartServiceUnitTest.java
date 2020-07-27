package com.vikas.cart.junit.services;

import com.vikas.cart.model.Cart;
import com.vikas.cart.model.CartItem;
import com.vikas.cart.model.Product;
import com.vikas.cart.services.CartService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class CartServiceUnitTest {

    @InjectMocks
    private CartService cartService;

    @Test
    public void shouldAddCartSuccessfully() {
        Cart responseCart = new Cart();
        responseCart.cartItems(new ArrayList<>());

        cartService.processCart(responseCart.getId().toString(), responseCart);

        assertThat(responseCart.getCartItems().size()).isEqualTo(0);
        assertThat(responseCart.getTotalNumberOfItemsInCart()).isEqualTo(0);
    }

    @Test
    public void shouldUpdateCartSuccessfully() {
        Cart responseCart = new Cart();
        responseCart.cartItems(new ArrayList<>());

        Product soap = new Product();
        soap.setName("Dove Soap");
        soap.setPrice(39.99f);

        CartItem soapItem = new CartItem();
        soapItem.setQuantity(5);
        soapItem.setProduct(soap);
        responseCart.addCartItem(soapItem);

        cartService.processCart(responseCart.getId().toString(), responseCart);

        assertThat(responseCart.getCartItems().size()).isEqualTo(1);
        assertThat(responseCart.getTotalNumberOfItemsInCart()).isEqualTo(5);
        assertThat(responseCart.getCartPriceWithoutTax()).isEqualTo(199.95f);

        Product laptop = new Product();
        laptop.setName("Laptop");
        laptop.setPrice(1000f);

        CartItem laptopItem = new CartItem();
        laptopItem.setQuantity(2);
        laptopItem.setProduct(laptop);
        responseCart.addCartItem(laptopItem);

        cartService.processCart(responseCart.getId().toString(), responseCart);

        assertThat(responseCart.getCartItems().size()).isEqualTo(2);
        assertThat(responseCart.getTotalNumberOfItemsInCart()).isEqualTo(7);
        assertThat(responseCart.getCartPriceWithoutTax()).isEqualTo(2199.95f);
    }

    @Test
    public void shouldUpdateCartTaxSuccessfully() {
        Cart responseCart = new Cart();
        responseCart.cartItems(new ArrayList<>());

        Product soap = new Product();
        soap.setName("Dove Soap");
        soap.setPrice(39.99f);

        CartItem soapItem = new CartItem();
        soapItem.setQuantity(5);
        soapItem.setProduct(soap);
        responseCart.addCartItem(soapItem);
        responseCart.setSalesTax(12.5f);

        cartService.processCart(responseCart.getId().toString(), responseCart);

        assertThat(responseCart.getCartItems().size()).isEqualTo(1);
        assertThat(responseCart.getTotalNumberOfItemsInCart()).isEqualTo(5);
        assertThat(responseCart.getCartPriceWithoutTax()).isEqualTo(199.95f);
        assertThat(responseCart.getCartPriceWithTax()).isEqualTo(224.95f);
    }

}
