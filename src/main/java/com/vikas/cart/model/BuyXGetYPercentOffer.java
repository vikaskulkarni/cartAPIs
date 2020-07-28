package com.vikas.cart.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BuyXGetYPercentOffer extends OfferCode {
    @Override
    public Float getDiscount(int quantity, Float price) {
        int totalItemTobeDiscounted = quantity / 2;
        //int diff = quantity - totalItemTobeDiscounted;
        double retVal = totalItemTobeDiscounted * (Math.ceil(0.5 * price));

        BigDecimal bd = new BigDecimal(retVal).setScale(2, RoundingMode.HALF_UP);
        return bd.floatValue();
    }
}
