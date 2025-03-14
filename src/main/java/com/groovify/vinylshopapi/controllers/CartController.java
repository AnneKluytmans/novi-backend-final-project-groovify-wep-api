package com.groovify.vinylshopapi.controllers;

import com.groovify.vinylshopapi.services.CartService;
import com.groovify.vinylshopapi.services.CustomerCartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(
            @PathVariable("id") Long cartId
    ) {
        cartService.deleteCart(cartId);
        return ResponseEntity.noContent().build();
    }
}
