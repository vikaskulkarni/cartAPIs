package com.vikas.cart.junit.services;

import com.vikas.cart.model.BuyXGetYOffer;
import com.vikas.cart.model.Cart;
import com.vikas.cart.model.CartItem;
import com.vikas.cart.model.OfferCode;
import com.vikas.cart.model.Product;
import com.vikas.cart.services.CartService;
import com.vikas.cart.services.OfferService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartServiceUnitTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private OfferService offerService;

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

    @Test
    public void shouldUpdateCartDiscountSuccessfully() {
        OfferCode offerCode = new BuyXGetYOffer();
        offerCode.setId(2);
        offerCode.setBuyCount(2);
        offerCode.setGetCount(1);
        when(offerService.findById(2, BuyXGetYOffer.class)).thenReturn(offerCode);

        Cart responseCart = new Cart();
        responseCart.cartItems(new ArrayList<>());

        Product soap = new Product();
        soap.setName("Dove Soap");
        soap.setPrice(39.99f);

        CartItem soapItem = new CartItem();
        soapItem.setQuantity(3);
        soapItem.setProduct(soap);
        soapItem.setOfferCode(2);
        responseCart.addCartItem(soapItem);
        responseCart.setSalesTax(12.5f);

        cartService.processCart(responseCart.getId().toString(), responseCart);

        assertThat(responseCart.getCartItems().size()).isEqualTo(1);
        assertThat(responseCart.getTotalNumberOfItemsInCart()).isEqualTo(3);
        assertThat(responseCart.getCartPriceWithoutTax()).isEqualTo(79.98f);
        assertThat(responseCart.getCartPriceWithTax()).isEqualTo(89.98f);

        assert 10.0 == (responseCart.getCartPriceWithTax() - responseCart.getCartPriceWithoutTax());
    }

    @Test
    public void shouldUpdateCartDiscountMultipleSuccessfully() {
        OfferCode offerCode = new BuyXGetYOffer();
        offerCode.setId(2);
        offerCode.setBuyCount(2);
        offerCode.setGetCount(1);
        when(offerService.findById(2, BuyXGetYOffer.class)).thenReturn(offerCode);

        Cart responseCart = new Cart();
        responseCart.cartItems(new ArrayList<>());

        Product soap = new Product();
        soap.setName("Dove Soap");
        soap.setPrice(39.99f);

        CartItem soapItem = new CartItem();
        soapItem.setQuantity(4);
        soapItem.setProduct(soap);
        soapItem.setOfferCode(2);
        responseCart.addCartItem(soapItem);
        responseCart.setSalesTax(12.5f);

        cartService.processCart(responseCart.getId().toString(), responseCart);

        assertThat(responseCart.getCartItems().size()).isEqualTo(1);
        assertThat(responseCart.getTotalNumberOfItemsInCart()).isEqualTo(4);
        assertThat(responseCart.getCartPriceWithoutTax()).isEqualTo(119.97f);
        assertThat(responseCart.getCartPriceWithTax()).isEqualTo(134.97f);

        assert 15.0 == (responseCart.getCartPriceWithTax() - responseCart.getCartPriceWithoutTax());
    }

    @Test
    public void shouldUpdateCartDiscountMultipleTimesSuccessfully() {
        OfferCode offerCode = new BuyXGetYOffer();
        offerCode.setId(2);
        offerCode.setBuyCount(2);
        offerCode.setGetCount(1);
        when(offerService.findById(2, BuyXGetYOffer.class)).thenReturn(offerCode);

        Cart responseCart = new Cart();
        responseCart.cartItems(new ArrayList<>());

        Product soap = new Product();
        soap.setName("Dove Soap");
        soap.setPrice(39.99f);

        CartItem soapItem = new CartItem();
        soapItem.setQuantity(3);
        soapItem.setProduct(soap);
        soapItem.setOfferCode(2);
        responseCart.addCartItem(soapItem);
        responseCart.setSalesTax(12.5f);

        cartService.processCart(responseCart.getId().toString(), responseCart);

        assertResponseCart(responseCart, soapItem, 1, 3,
                89.98f, 79.98f, 10.0f);

        responseCart = new Cart();
        responseCart.cartItems(new ArrayList<>());
        soapItem = new CartItem();
        soapItem.setQuantity(5);
        soapItem.setProduct(soap);
        soapItem.setOfferCode(2);
        responseCart.addCartItem(soapItem);
        responseCart.setSalesTax(12.5f);

        cartService.processCart(responseCart.getId().toString(), responseCart);

        assertResponseCart(responseCart, soapItem, 1, 5,
                179.96f, 159.96f, 20.0f);

        responseCart = new Cart();
        responseCart.cartItems(new ArrayList<>());
        soapItem = new CartItem();
        soapItem.setQuantity(3);
        soapItem.setProduct(soap);
        soapItem.setOfferCode(2);
        responseCart.addCartItem(soapItem);
        responseCart.setSalesTax(12.5f);

        Product axeSoap = new Product();
        axeSoap.setName("Axe Soap");
        axeSoap.setPrice(89.99f);

        CartItem axeSoapItem = new CartItem();
        axeSoapItem.setQuantity(2);
        axeSoapItem.setProduct(axeSoap);
        responseCart.addCartItem(axeSoapItem);

        cartService.processCart(responseCart.getId().toString(), responseCart);

        assertResponseCart(responseCart, soapItem, 2, 5,
                292.96f, 259.96f, 33f);
    }

    private void assertResponseCart(Cart responseCart, CartItem cartItem,
                                    int cartItems, int totalItems,
                                    float cartPriceWithTax, float cartPriceWithoutTax,
                                    float totalTax) {
        float totalCartAmount;
        BigDecimal da;
        float totalDiscount;
        BigDecimal td;
        assertThat(responseCart.getCartItems().size()).isEqualTo(cartItems);
        assertThat(responseCart.getTotalNumberOfItemsInCart()).isEqualTo(totalItems);
        assertThat(responseCart.getCartPriceWithoutTax()).isEqualTo(cartPriceWithoutTax);
        assertThat(responseCart.getCartPriceWithTax()).isEqualTo(cartPriceWithTax);

        totalCartAmount = 0;
        for (CartItem item : responseCart.getCartItems()) {
            totalCartAmount += item.getProduct().getPrice() * item.getQuantity();
        }

        da = new BigDecimal(totalCartAmount).setScale(2, RoundingMode.HALF_UP);
        totalDiscount = da.floatValue() - responseCart.getCartPriceWithoutTax();

        td = new BigDecimal(totalDiscount).setScale(2, RoundingMode.HALF_UP);

        assert cartItem.getProduct().getPrice() == td.floatValue();

        assert totalTax == (responseCart.getCartPriceWithTax() - responseCart.getCartPriceWithoutTax());
    }

}
