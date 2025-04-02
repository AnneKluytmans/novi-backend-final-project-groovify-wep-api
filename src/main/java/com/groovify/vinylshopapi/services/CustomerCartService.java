package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.CartItemRequestDTO;
import com.groovify.vinylshopapi.dtos.CartItemQuantityUpdateDTO;
import com.groovify.vinylshopapi.dtos.CartResponseDTO;
import com.groovify.vinylshopapi.exceptions.RecordNotFoundException;
import com.groovify.vinylshopapi.mappers.CartItemMapper;
import com.groovify.vinylshopapi.mappers.CartMapper;
import com.groovify.vinylshopapi.models.Cart;
import com.groovify.vinylshopapi.models.CartItem;
import com.groovify.vinylshopapi.models.Customer;
import com.groovify.vinylshopapi.models.VinylRecord;
import com.groovify.vinylshopapi.repositories.CartRepository;
import com.groovify.vinylshopapi.repositories.CustomerRepository;
import com.groovify.vinylshopapi.repositories.VinylRecordRepository;
import com.groovify.vinylshopapi.validation.ValidationUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class CustomerCartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;
    private final CustomerRepository customerRepository;
    private final VinylRecordRepository vinylRecordRepository;

    public CustomerCartService(
            CartRepository cartRepository,
            CartMapper cartMapper,
            CartItemMapper cartItemMapper,
            CustomerRepository customerRepository,
            VinylRecordRepository vinylRecordRepository
    ) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.cartItemMapper = cartItemMapper;
        this.customerRepository = customerRepository;
        this.vinylRecordRepository = vinylRecordRepository;
    }

    public CartResponseDTO getCustomerCart(Long customerId) {
        return cartMapper.toResponseDTO(findCart(customerId));
    }

    public CartResponseDTO addCartItemToCart(Long customerId, CartItemRequestDTO cartItemRequestDTO) {
        Cart cart = findOrCreateCart(customerId);
        VinylRecord vinylRecord = findVinylRecord(cartItemRequestDTO.getVinylRecordId());
        CartItem existingCartItem = findExistingCartItem(cart, vinylRecord);

        if (existingCartItem != null) {
            int newQuantity = existingCartItem.getQuantity() + cartItemRequestDTO.getQuantity();
            ValidationUtils.validateVinylRecordStock(vinylRecord, newQuantity);
            existingCartItem.setQuantity(newQuantity);
        } else {
            ValidationUtils.validateVinylRecordStock(vinylRecord, cartItemRequestDTO.getQuantity());
            CartItem cartItem = cartItemMapper.toEntity(cartItemRequestDTO);
            cartItem.setVinylRecord(vinylRecord);
            cartItem.setCart(cart);
            cart.getCartItems().add(cartItem);
        }

        cart.setUpdatedAt(LocalDateTime.now());

        return cartMapper.toResponseDTO(cartRepository.save(cart));
    }

    public CartResponseDTO updateCartItemQuantity(Long customerId, Long cartItemId, CartItemQuantityUpdateDTO cartItemQuantityDTO) {
        Cart cart = findCart(customerId);
        CartItem cartItem = findCartItem(cart, cartItemId);

        ValidationUtils.validateVinylRecordStock(cartItem.getVinylRecord(), cartItemQuantityDTO.getNewQuantity());

        cartItem.setQuantity(cartItemQuantityDTO.getNewQuantity());
        cart.setUpdatedAt(LocalDateTime.now());

        return cartMapper.toResponseDTO(cartRepository.save(cart));
    }

    public CartResponseDTO removeCartItemFromCart(Long customerId, Long cartItemId) {
        Cart cart = findCart(customerId);

        cart.getCartItems().remove(findCartItem(cart, cartItemId));
        cart.setUpdatedAt(LocalDateTime.now());

        return cartMapper.toResponseDTO(cartRepository.save(cart));
    }

    public CartResponseDTO clearCart(Long customerId) {
        Cart cart = findCart(customerId);

        cart.getCartItems().clear();
        cart.setUpdatedAt(LocalDateTime.now());

        return cartMapper.toResponseDTO(cartRepository.save(cart));
    }


    private Customer findCustomer(Long customerId) {
        return customerRepository.findByIdAndIsDeletedFalse(customerId)
                .orElseThrow(() -> new RecordNotFoundException("No customer found with id: " + customerId));
    }

    private VinylRecord findVinylRecord(Long vinylRecordId) {
        return vinylRecordRepository.findById(vinylRecordId)
                .orElseThrow(() -> new RecordNotFoundException("No vinyl record found with id: " + vinylRecordId));
    }

    private Cart findCart(Long customerId) {
        return cartRepository.findByCustomerIdAndCustomerIsDeletedFalse(customerId)
                .orElseThrow(() -> new RecordNotFoundException("No cart found for customer with id: " + customerId));
    }

    private Cart findOrCreateCart(Long customerId) {
        return cartRepository.findByCustomerIdAndCustomerIsDeletedFalse(customerId)
                .orElseGet(() -> {
                    Cart newCart = new Cart(findCustomer(customerId));
                    return cartRepository.save(newCart);
                });
    }

    private CartItem findCartItem(Cart cart, Long cartItemId) {
        for (CartItem cartItem : cart.getCartItems()) {
            if (cartItem.getId().equals(cartItemId)) {
                return cartItem;
            }
        }
        throw new RecordNotFoundException("No cart item found with id: " + cartItemId + " in the cart of customer with id: "
                + cart.getCustomer().getId());
    }

    private CartItem findExistingCartItem(Cart cart, VinylRecord vinylRecord) {
        for (CartItem cartItem : cart.getCartItems()) {
            if (cartItem.getVinylRecord().equals(vinylRecord)) {
                return cartItem;
            }
        }
        return null;
    }

}
