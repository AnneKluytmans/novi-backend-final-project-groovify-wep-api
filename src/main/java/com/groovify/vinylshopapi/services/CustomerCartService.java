package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.CartItemRequestDTO;
import com.groovify.vinylshopapi.dtos.CartItemUpdateQuantityDTO;
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

import java.time.LocalDateTime;


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

    public CartResponseDTO getCustomerCart(Long customerId) {
        return cartMapper.toResponseDTO(findCart(customerId));
    }

    public CartResponseDTO createCart(Long customerId) {
        if (cartRepository.existsByCustomerId(customerId)) {
            throw new ConflictException("Customer already has a cart");
        }

        Cart cart = new Cart();
        cart.setCustomer(findCustomer(customerId));
        cart.setCreatedAt(LocalDateTime.now());

        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toResponseDTO(savedCart);
    }

    public CartResponseDTO clearCart(Long customerId) {
        Cart cart = findCart(customerId);

        cart.getCartItems().clear();
        cart.setUpdatedAt(LocalDateTime.now());

        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toResponseDTO(savedCart);
    }

    public CartResponseDTO addCartItemToCart(Long customerId, CartItemRequestDTO cartItemRequestDTO) {
        Cart cart = findCart(customerId);
        VinylRecord vinylRecord = findVinylRecord(cartItemRequestDTO.getVinylRecordId());

        for (CartItem cartItemInCart : cart.getCartItems()) {
            if (cartItemInCart.getVinylRecord().equals(vinylRecord)) {
                throw new ConflictException("The vinyl record '" + vinylRecord.getTitle() +
                        "' is already in your cart. You can update the quantity of this item.");
            }
        }

        CartItem cartItem = cartItemMapper.toEntity(cartItemRequestDTO);
        cartItem.setVinylRecord(vinylRecord);
        cartItem.setCart(cart);
        cart.getCartItems().add(cartItem);
        cart.setUpdatedAt(LocalDateTime.now());

        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toResponseDTO(savedCart);
    }

    public CartResponseDTO removeCartItemFromCart(Long customerId, Long cartItemId) {
        Cart cart = findCart(customerId);
        CartItem cartItem = findCartItem(cartItemId);

        if (!cart.getCartItems().contains(cartItem)) {
            throw new DeleteOperationException("Cart item not found in this cart");
        }

        cart.getCartItems().remove(cartItem);
        cart.setUpdatedAt(LocalDateTime.now());

        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toResponseDTO(savedCart);
    }

    public CartResponseDTO updateCartItemQuantity(Long customerId, Long cartItemId, CartItemUpdateQuantityDTO cartItemQuantityDTO) {
        Cart cart = findCart(customerId);
        CartItem cartItem = findCartItem(cartItemId);

        if (!cart.getCartItems().contains(cartItem)) {
            throw new DeleteOperationException("Cart item not found in this cart");
        }

        cartItem.setQuantity(cartItemQuantityDTO.getNewQuantity());
        cart.setUpdatedAt(LocalDateTime.now());

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
