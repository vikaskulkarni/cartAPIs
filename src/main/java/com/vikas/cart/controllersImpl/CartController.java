package com.vikas.cart.controllersImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.vikas.cart.IControllers.ICart;
import com.vikas.cart.model.Cart;
import com.vikas.cart.model.CartItem;
import com.vikas.cart.model.Product;
import com.vikas.cart.services.CartService;
import com.vikas.cart.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
public class CartController implements ICart {

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Override
    public Cart createCart(Cart cart) throws RestClientException {
        Cart responseCart = new Cart();
        responseCart.cartItems(new ArrayList<>());

        if (cart.getCartItems() != null && cart.getCartItems().size() > 0) {
            responseCart = cart;
        }

        cartService.processCart(responseCart.getId().toString(), responseCart);
        return responseCart;
    }

    @Override
    public ResponseEntity<Cart> getCart(String id) throws RestClientException {
        return cartService.getCart(id) == null ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).build() :
                ResponseEntity.ok(cartService.getCart(id));
    }

    @Override
    public ResponseEntity<Cart> updateCart(String id, JsonPatch patch) throws RestClientException, JsonPatchException, JsonProcessingException {
        Cart cartInSession = cartService.getCart(id);
        if (cartInSession == null)
            throw new RuntimeException("Cart Not found");
        try {
//            checkForItemsInCart(cartInSession.getCartItems(), patch);
            Cart updatedCart = applyPatchToCart(patch, cartInSession);

            updatedCart = cartService.processCart(id, updatedCart);
            return ResponseEntity.ok(updatedCart);
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Override
    public ResponseEntity<Cart> modifyCart(String id, Cart cart) throws RestClientException, JsonPatchException, JsonProcessingException {
        Cart cartInSession = cartService.getCart(id);
        List<CartItem> newList = new ArrayList<>();

        for (CartItem item : cart.getCartItems()) {
            boolean existingPrd = false;
            Product prod = item.getProduct();

            for (CartItem existingItem : cartInSession.getCartItems()) {
                Product existingProd = existingItem.getProduct();
                if (existingProd.getName().equals(prod.getName())) {
                    int quantityTobeUpdated = item.getQuantity();
                    existingItem.setQuantity(quantityTobeUpdated + existingItem.getQuantity());
                    existingPrd = true;
                    break;
                }
            }
            if (!existingPrd) {
                cartInSession.getCartItems().add(item);
            }

        }


        return ResponseEntity.ok(cartInSession);
    }

    private void checkForItemsInCart(List<CartItem> cartItems, JsonPatch patch) {


    }

    private Cart applyPatchToCart(
            JsonPatch patch, Cart cartToBeUpdated) throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode patched = patch.apply(objectMapper.convertValue(cartToBeUpdated, JsonNode.class));
        return objectMapper.treeToValue(patched, Cart.class);
    }
}
