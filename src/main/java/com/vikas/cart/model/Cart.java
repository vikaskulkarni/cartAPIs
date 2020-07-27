package com.vikas.cart.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Cart
 */
public class Cart {
    @JsonProperty("id")
    private UUID id = UUID.randomUUID();

    @JsonProperty("salesTax")
    private Float salesTax = 0f;

    @JsonProperty("cartPriceWithTax")
    private Float cartPriceWithTax = 0f;

    @JsonProperty("cartPriceWithoutTax")
    private Float cartPriceWithoutTax = 0f;

    @JsonProperty("cartItems")
    private List<CartItem> cartItems;

    @JsonProperty("totalNumberOfItemsInCart")
    private Integer totalNumberOfItemsInCart = 0;

    public Cart id(UUID id) {
        this.id = id;
        return this;
    }

    /**
     * Get id
     *
     * @return id
     **/
    @ApiModelProperty(value = "")
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Cart salesTax(Float salesTax) {
        this.salesTax = salesTax;
        return this;
    }

    /**
     * Get salesTax
     *
     * @return salesTax
     **/
    @ApiModelProperty(value = "")
    public Float getSalesTax() {
        return salesTax;
    }

    public void setSalesTax(Float salesTax) {
        this.salesTax = salesTax;
    }

    public Cart cartPriceWithTax(Float cartPriceWithTax) {
        this.cartPriceWithTax = cartPriceWithTax;
        return this;
    }

    /**
     * Get cartPriceWithTax
     *
     * @return cartPriceWithTax
     **/
    @ApiModelProperty(value = "")
    public Float getCartPriceWithTax() {
        return cartPriceWithTax;
    }

    public void setCartPriceWithTax(Float cartPriceWithTax) {
        this.cartPriceWithTax = cartPriceWithTax;
    }

    public Cart cartPriceWithoutTax(Float cartPriceWithoutTax) {
        this.cartPriceWithoutTax = cartPriceWithoutTax;
        return this;
    }

    /**
     * Get cartPriceWithoutTax
     *
     * @return cartPriceWithoutTax
     **/
    @ApiModelProperty(value = "")
    public Float getCartPriceWithoutTax() {
        return cartPriceWithoutTax;
    }

    public void setCartPriceWithoutTax(Float cartPriceWithoutTax) {
        this.cartPriceWithoutTax = cartPriceWithoutTax;
    }

    public Cart cartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
        return this;
    }

    public Cart addCartItem(CartItem cartItem) {
        if (this.cartItems == null) {
            this.cartItems = new ArrayList<>();
        }
        this.cartItems.add(cartItem);
        return this;
    }

    /**
     * Get cart items
     *
     * @return cart items
     **/
    @ApiModelProperty(value = "")
    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public Cart totalNumberOfItemsInCart(Integer totalNumberOfItemsInCart) {
        this.totalNumberOfItemsInCart = totalNumberOfItemsInCart;
        return this;
    }

    /**
     * Get totalNumberOfItemsInCart
     *
     * @return totalNumberOfItemsInCart
     **/
    @ApiModelProperty(value = "")
    public Integer getTotalNumberOfItemsInCart() {
        return totalNumberOfItemsInCart;
    }

    public void setTotalNumberOfItemsInCart(Integer totalNumberOfItemsInCart) {
        this.totalNumberOfItemsInCart = totalNumberOfItemsInCart;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cart cart = (Cart) o;
        return Objects.equals(this.id, cart.id) &&
                Objects.equals(this.salesTax, cart.salesTax) &&
                Objects.equals(this.cartPriceWithTax, cart.cartPriceWithTax) &&
                Objects.equals(this.cartPriceWithoutTax, cart.cartPriceWithoutTax) &&
                Objects.equals(this.cartItems, cart.cartItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, salesTax, cartPriceWithTax, cartPriceWithoutTax, cartItems);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Cart {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    salesTax: ").append(toIndentedString(salesTax)).append("\n");
        sb.append("    cartPriceWithTax: ").append(toIndentedString(cartPriceWithTax)).append("\n");
        sb.append("    cartPriceWithoutTax: ").append(toIndentedString(cartPriceWithoutTax)).append("\n");
        sb.append("    cartItems: ").append(toIndentedString(cartItems)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

}

