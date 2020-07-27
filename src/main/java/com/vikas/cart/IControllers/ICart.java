package com.vikas.cart.IControllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.vikas.cart.model.Cart;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestClientException;

@RequestMapping("/cart")
public interface ICart {

    /**
     * Creates a Cart
     *
     * <p><b>201</b> - Created
     * <p><b>400</b> - Bad Request
     * <p><b>500</b> - Internal Server Error
     *
     * @param cart The Cart to be created
     * @return Cart
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    @PostMapping(path = "", produces = "application/json")
    Cart createCart(@RequestBody(required = false) Cart cart) throws RestClientException;

    /**
     * Gets a Cart by Id
     *
     * <p><b>200</b> - Ok
     * <p><b>400</b> - Bad Request
     * <p><b>500</b> - Internal Server Error
     *
     * @param id The id parameter
     * @return Cart
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    @GetMapping(path = "/{id}", produces = "application/json")
    ResponseEntity<Cart> getCart(@PathVariable String id) throws RestClientException;

    /**
     * Updates a Cart
     *
     * <p><b>200</b> - Updated
     * <p><b>400</b> - Bad Request
     * <p><b>500</b> - Internal Server Error
     *
     * @param id The id parameter
     * @return Cart
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    ResponseEntity<Cart> updateCart(@PathVariable String id, @RequestBody JsonPatch patch) throws RestClientException, JsonPatchException, JsonProcessingException;
}
