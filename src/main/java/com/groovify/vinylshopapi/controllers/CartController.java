package com.groovify.vinylshopapi.controllers;

import com.groovify.vinylshopapi.dtos.CartResponseDTO;
import com.groovify.vinylshopapi.services.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
            @RequestParam(required = false) LocalDate createdBefore,
            @RequestParam(required = false) LocalDate createdAfter,
            @RequestParam(required = false) LocalDate updatedBefore,
            @RequestParam(required = false) LocalDate updatedAfter,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Boolean isEmpty,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder
    ) {
        List<CartResponseDTO> carts = cartService.getAllCarts(
                createdBefore, createdAfter, updatedBefore, updatedAfter, customerId,
                isEmpty, sortBy, sortOrder
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
