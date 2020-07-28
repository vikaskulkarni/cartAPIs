package com.vikas.cart.services;

import com.vikas.cart.model.BuyXGetYOffer;
import com.vikas.cart.model.BuyXGetYPercentOffer;
import com.vikas.cart.model.Cart;
import com.vikas.cart.model.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
public class CartService {
    @Autowired
    OfferService offerService;

    Map<String, Cart> cartsInSession = new HashMap<>();
    Integer currentTotalItems;
    Float cartTotalPriceWithoutTax;
    Float cartTotalPriceWithTax;
    Float discount;

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
        discount = 0.0f;

        cart.getCartItems().forEach(this::processItem);

        cartTotalPriceWithoutTax = cartTotalPriceWithoutTax - discount;
        cartTotalPriceWithTax = cartTotalPriceWithoutTax;

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
        int quantity = cartItem.getQuantity();
        currentTotalItems = currentTotalItems + quantity;

        cartTotalPriceWithoutTax = cartTotalPriceWithoutTax +
                (cartItem.getProduct().getPrice() * quantity);

        switch (cartItem.getOfferCode()) {
            case 2:
                BuyXGetYOffer offerCode = (BuyXGetYOffer) offerService.findById(cartItem.getOfferCode(), BuyXGetYOffer.class);
                discount = offerCode.getDiscount(quantity, cartItem.getProduct().getPrice());
                break;
            case 3:
                BuyXGetYPercentOffer offerCodePercent = (BuyXGetYPercentOffer) offerService.findById(cartItem.getOfferCode(), BuyXGetYPercentOffer.class);
                discount = offerCodePercent.getDiscount(quantity, cartItem.getProduct().getPrice());
                break;
            case 4:
                if (cartTotalPriceWithoutTax >= 500)
                    discount = (float) 0.2 * cartTotalPriceWithoutTax;
                break;
        }
    }
}
