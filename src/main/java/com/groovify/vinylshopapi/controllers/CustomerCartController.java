package com.groovify.vinylshopapi.controllers;

import com.groovify.vinylshopapi.dtos.CartItemRequestDTO;
import com.groovify.vinylshopapi.dtos.CartItemUpdateQuantityDTO;
import com.groovify.vinylshopapi.dtos.CartResponseDTO;
import com.groovify.vinylshopapi.services.CustomerCartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/customers/{id}/cart")
public class CustomerCartController {

    private final CustomerCartService customerCartService;

    public CustomerCartController(CustomerCartService customerCartService) {
        this.customerCartService = customerCartService;
    }

    @PostMapping()
    public ResponseEntity<CartResponseDTO> createCart(
            @PathVariable("id") Long customerId
    ) {
        CartResponseDTO newCart = customerCartService.createCart(customerId);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .build()
                .toUri();
        return ResponseEntity.created(location).body(newCart);
    }

    @DeleteMapping()
    public ResponseEntity<CartResponseDTO> clearCart(
            @PathVariable("id") Long customerId
    ) {
        CartResponseDTO cart = customerCartService.clearCart(customerId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/cart-items")
    public ResponseEntity<?> addCartItemToCart(
            @PathVariable("id") Long customerId,
            @Valid @RequestBody CartItemRequestDTO cartItemRequestDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        CartResponseDTO cart = customerCartService.addCartItemToCart(customerId, cartItemRequestDTO);
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/cart-items/{itemId}")
    public ResponseEntity<?> updateCartItemQuantity(
            @PathVariable("id") Long customerId,
            @PathVariable("itemId") Long cartItemId,
            @Valid @RequestBody CartItemUpdateQuantityDTO cartItemQuantityDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        CartResponseDTO cart = customerCartService.updateCartItemQuantity(customerId, cartItemId, cartItemQuantityDTO);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/cart-items/{itemId}")
    public ResponseEntity<CartResponseDTO> removeCartItemFromCart(
            @PathVariable("id") Long customerId,
            @PathVariable("itemId") Long cartItemId
    ) {
        CartResponseDTO cart = customerCartService.removeCartItemFromCart(customerId, cartItemId);
        return ResponseEntity.ok(cart);
    }
}
