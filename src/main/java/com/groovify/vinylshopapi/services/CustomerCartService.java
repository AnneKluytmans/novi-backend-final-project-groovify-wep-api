package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.CartItemRequestDTO;
import com.groovify.vinylshopapi.dtos.CartResponseDTO;
import com.groovify.vinylshopapi.exceptions.ConflictException;
import com.groovify.vinylshopapi.exceptions.DeleteOperationException;
import com.groovify.vinylshopapi.exceptions.RecordNotFoundException;
import com.groovify.vinylshopapi.mappers.CartItemMapper;
import com.groovify.vinylshopapi.mappers.CartMapper;
import com.groovify.vinylshopapi.models.Cart;
import com.groovify.vinylshopapi.models.CartItem;
import com.groovify.vinylshopapi.models.Customer;
import com.groovify.vinylshopapi.models.VinylRecord;
import com.groovify.vinylshopapi.repositories.CartItemRepository;
import com.groovify.vinylshopapi.repositories.CartRepository;
import com.groovify.vinylshopapi.repositories.CustomerRepository;
import com.groovify.vinylshopapi.repositories.VinylRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerCartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CustomerRepository customerRepository;
    private final VinylRecordRepository vinylRecordRepository;
    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;

    public CustomerCartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            CustomerRepository customerRepository,
            VinylRecordRepository vinylRecordRepository,
            CartMapper cartMapper,
            CartItemMapper cartItemMapper
    ) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.customerRepository = customerRepository;
        this.vinylRecordRepository = vinylRecordRepository;
        this.cartMapper = cartMapper;
        this.cartItemMapper = cartItemMapper;
    }


    public CartResponseDTO createCart(Long customerId) {
        if (cartRepository.existsByCustomerId(customerId)) {
            throw new ConflictException("Customer already has a cart");
        }

        Cart cart = new Cart();
        cart.setCustomer(findCustomer(customerId));

        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toResponseDTO(savedCart);
    }

    public void deleteCart(Long customerId) {
        Cart cart = findCart(customerId);
        if (!cart.getCartItems().isEmpty()) {
            throw new DeleteOperationException("Cart is not empty and cannot be deleted");
        }
        cartRepository.delete(cart);
    }

    public CartResponseDTO addCartItemToCart(Long customerId, CartItemRequestDTO cartItemRequestDTO) {
        Cart cart = findCart(customerId);
        VinylRecord vinylRecord = findVinylRecord(cartItemRequestDTO.getVinylRecordId());
        CartItem cartItem = cartItemMapper.toEntity(cartItemRequestDTO);
        cartItem.setVinylRecord(vinylRecord);

        List<CartItem> cartItemsInCart = cart.getCartItems();

        for (CartItem cartItemInCart : cartItemsInCart) {
            if (cartItemInCart.getVinylRecord().equals(vinylRecord)) {
                throw new ConflictException("The vinyl record '" + vinylRecord.getTitle() +
                        "' is already in your cart. You can update the quantity of this item.");
            }
        }

        cart.getCartItems().add(cartItem);

        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toResponseDTO(savedCart);
    }

    public CartResponseDTO deleteCartItemFromCart(Long customerId, Long cartItemId) {
        Cart cart = findCart(customerId);
        CartItem cartItem = findCartItem(cartItemId);

        if (!cart.getCartItems().contains(cartItem)) {
            throw new DeleteOperationException("Cart item not found in this cart");
        }

        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);

        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toResponseDTO(savedCart);
    }


    private Customer findCustomer(Long customerId) {
        return customerRepository.findByIdAndIsDeletedFalse(customerId)
                .orElseThrow(() -> new RecordNotFoundException("Customer with id " + customerId + " not found"));
    }

    private VinylRecord findVinylRecord(Long vinylRecordId) {
        return vinylRecordRepository.findById(vinylRecordId)
                .orElseThrow(() -> new RecordNotFoundException("Vinyl record with id " + vinylRecordId + " not found"));
    }

    private Cart findCart(Long customerId) {
        return cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new RecordNotFoundException("Cart of customer with id " + customerId + " not found"));
    }

    private CartItem findCartItem(Long cartItemId) {
        return cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RecordNotFoundException("Cart item with id " + cartItemId + " not found"));
    }


}
