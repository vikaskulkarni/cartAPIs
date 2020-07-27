package com.vikas.cart.services;

import com.vikas.cart.model.Cart;
import com.vikas.cart.model.CartItem;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
public class CartService {
    Map<String, Cart> cartsInSession = new HashMap<>();
    Integer currentTotalItems;
    Float cartTotalPriceWithoutTax;
    Float cartTotalPriceWithTax;

    public Cart getCart(String id) {
        return cartsInSession.get(id);
    }

    public Cart processCart(String id, Cart cart) {
        calculateTotalPrice(cart);
        cartsInSession.put(id, cart);
        return cart;
    }

    private void calculateTotalPrice(Cart cart) {
        currentTotalItems = 0;
        cartTotalPriceWithoutTax = 0f;
        cartTotalPriceWithTax = 0f;

        cart.getCartItems().forEach(this::processItem);

        if (cart.getSalesTax() != 0.0) {
            float totalTaxOnCart = (float) Math.ceil((cartTotalPriceWithoutTax * (cart.getSalesTax() / 100)));
            cartTotalPriceWithTax = cartTotalPriceWithoutTax + totalTaxOnCart;
        }

        cart.setTotalNumberOfItemsInCart(currentTotalItems);

        BigDecimal bdWo = new BigDecimal(cartTotalPriceWithoutTax).setScale(2, RoundingMode.HALF_UP);
        cart.setCartPriceWithoutTax(bdWo.floatValue());

        BigDecimal bdW = new BigDecimal(cartTotalPriceWithTax).setScale(2, RoundingMode.HALF_UP);
        cart.setCartPriceWithTax(bdW.floatValue());
    }

    private void processItem(CartItem cartItem) {
        currentTotalItems = currentTotalItems + cartItem.getQuantity();

        cartTotalPriceWithoutTax = cartTotalPriceWithoutTax +
                (cartItem.getProduct().getPrice() * cartItem.getQuantity());
        cartTotalPriceWithTax = cartTotalPriceWithTax +
                (cartItem.getProduct().getPrice() * cartItem.getQuantity());
    }
}
