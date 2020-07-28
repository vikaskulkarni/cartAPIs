package com.vikas.cart.model;

public class BuyXGetYOffer extends OfferCode {
    @Override
    public Float getDiscount(int quantity, Float price) {
        return quantity > 2 ? price : 0;
    }
}
