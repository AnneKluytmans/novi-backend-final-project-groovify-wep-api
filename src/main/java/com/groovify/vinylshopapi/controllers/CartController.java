package com.groovify.vinylshopapi.controllers;

import com.groovify.vinylshopapi.services.CartService;
import com.groovify.vinylshopapi.services.CustomerCartService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

}
