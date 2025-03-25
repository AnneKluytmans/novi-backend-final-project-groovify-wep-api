package com.groovify.vinylshopapi.controllers;

import com.groovify.vinylshopapi.dtos.CartItemRequestDTO;
import com.groovify.vinylshopapi.dtos.CartItemQuantityUpdateDTO;
import com.groovify.vinylshopapi.dtos.CartResponseDTO;
import com.groovify.vinylshopapi.services.CustomerCartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/customers/{id}/cart")
public class CustomerCartController {

    private final CustomerCartService customerCartService;

    public CustomerCartController(CustomerCartService customerCartService) {
        this.customerCartService = customerCartService;
    }

    @GetMapping()
    public ResponseEntity<CartResponseDTO> getCart(
            @PathVariable("id") Long customerId
    ) {
        CartResponseDTO cart = customerCartService.getCustomerCart(customerId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/items")
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

    @PatchMapping("/items/{itemId}")
    public ResponseEntity<?> updateCartItemQuantity(
            @PathVariable("id") Long customerId,
            @PathVariable("itemId") Long cartItemId,
            @Valid @RequestBody CartItemQuantityUpdateDTO cartItemQuantityDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        CartResponseDTO cart = customerCartService.updateCartItemQuantity(customerId, cartItemId, cartItemQuantityDTO);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CartResponseDTO> removeCartItemFromCart(
            @PathVariable("id") Long customerId,
            @PathVariable("itemId") Long cartItemId
    ) {
        CartResponseDTO cart = customerCartService.removeCartItemFromCart(customerId, cartItemId);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/items")
    public ResponseEntity<CartResponseDTO> clearCart(
            @PathVariable("id") Long customerId
    ) {
        CartResponseDTO cart = customerCartService.clearCart(customerId);
        return ResponseEntity.ok(cart);
    }
}
