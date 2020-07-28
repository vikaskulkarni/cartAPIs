package com.vikas.cart.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;
import java.util.UUID;

/**
 * CartItem
 */
public class CartItem {
    @JsonProperty("id")
    private UUID id = UUID.randomUUID();

    @JsonProperty("quantity")
    private Integer quantity = 0;

    @JsonProperty("product")
    private Product product = null;

    @JsonProperty("offerCode")
    private int offerCode = 0;

    public CartItem id(UUID id) {
        this.id = id;
        return this;
    }

    /**
     * Identifier of the cart item
     *
     * @return id
     **/
    @ApiModelProperty(value = "Identifier of the cart item")
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public CartItem quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    /**
     * Quantity of the product
     *
     * @return quantity
     **/
    @ApiModelProperty(value = "Quantity of the product")
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public CartItem product(Product product) {
        this.product = product;
        return this;
    }

    /**
     * Get product
     *
     * @return product
     **/
    @ApiModelProperty(value = "")
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * Get offerCode
     *
     * @return offerCode
     **/
    @ApiModelProperty(value = "")
    public int getOfferCode() {
        return offerCode;
    }

    public void setOfferCode(int offerCode) {
        this.offerCode = offerCode;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CartItem cartItem = (CartItem) o;
        return Objects.equals(this.id, cartItem.id) &&
                Objects.equals(this.quantity, cartItem.quantity) &&
                Objects.equals(this.product, cartItem.product) &&
                Objects.equals(this.offerCode, cartItem.offerCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quantity, product, offerCode);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CartItem {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
        sb.append("    product: ").append(toIndentedString(product)).append("\n");
        sb.append("    offerCode: ").append(toIndentedString(offerCode)).append("\n");
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

