package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.mappers.CartItemMapper;
import com.groovify.vinylshopapi.mappers.CartMapper;
import com.groovify.vinylshopapi.repositories.CartItemRepository;
import com.groovify.vinylshopapi.repositories.CartRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;

    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            CartMapper cartMapper,
            CartItemMapper cartItemMapper
    ) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartMapper = cartMapper;
        this.cartItemMapper = cartItemMapper;
    }

}
