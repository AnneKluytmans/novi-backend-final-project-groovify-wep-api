package com.groovify.vinylshopapi.controllers;

import com.groovify.vinylshopapi.dtos.CartResponseDTO;
import com.groovify.vinylshopapi.services.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }


    @GetMapping()
    public ResponseEntity<List<CartResponseDTO>> getAllCarts(
            @RequestParam(required = false) String createdBefore,
            @RequestParam(required = false) String createdAfter,
            @RequestParam(required = false) String updatedBefore,
            @RequestParam(required = false) String updatedAfter,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Boolean isEmpty,
            @RequestParam(required = false) Integer minAmountOfItems,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder
    ) {
        List<CartResponseDTO> carts = cartService.getAllCarts(
                createdBefore, createdAfter, updatedBefore, updatedAfter, customerId,
                isEmpty, minAmountOfItems, sortBy, sortOrder
        );
        return ResponseEntity.ok(carts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartResponseDTO> getCartById(
            @PathVariable Long id
    ) {
        CartResponseDTO cart = cartService.getCartById(id);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(
            @PathVariable Long id
    ) {
        cartService.deleteCart(id);
        return ResponseEntity.noContent().build();
    }
}
