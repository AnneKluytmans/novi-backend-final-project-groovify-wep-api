package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.CartItemRequestDTO;
import com.groovify.vinylshopapi.dtos.CartItemUpdateQuantityDTO;
import com.groovify.vinylshopapi.dtos.CartResponseDTO;
import com.groovify.vinylshopapi.exceptions.ConflictException;
import com.groovify.vinylshopapi.exceptions.InsufficientStockException;
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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class CustomerCartService {

    private final CartRepository cartRepository;
    private final CustomerRepository customerRepository;
    private final VinylRecordRepository vinylRecordRepository;
    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;

    public CustomerCartService(
            CartRepository cartRepository,
            CustomerRepository customerRepository,
            VinylRecordRepository vinylRecordRepository,
            CartMapper cartMapper,
            CartItemMapper cartItemMapper
    ) {
        this.cartRepository = cartRepository;
        this.customerRepository = customerRepository;
        this.vinylRecordRepository = vinylRecordRepository;
        this.cartMapper = cartMapper;
        this.cartItemMapper = cartItemMapper;
    }

    public CartResponseDTO getCustomerCart(Long customerId) {
        return cartMapper.toResponseDTO(findCart(customerId));
    }

    public CartResponseDTO addCartItemToCart(Long customerId, CartItemRequestDTO cartItemRequestDTO) {
        Cart cart = findOrCreateCart(customerId);
        VinylRecord vinylRecord = findVinylRecord(cartItemRequestDTO.getVinylRecordId());
        validateCartItemNotInCart(cart, vinylRecord);
        validateVinylRecordStock(vinylRecord, cartItemRequestDTO.getQuantity());

        CartItem cartItem = cartItemMapper.toEntity(cartItemRequestDTO);
        cartItem.setVinylRecord(vinylRecord);
        cartItem.setCart(cart);
        cart.getCartItems().add(cartItem);
        cart.setUpdatedAt(LocalDateTime.now());

        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toResponseDTO(savedCart);
    }

    public CartResponseDTO updateCartItemQuantity(Long customerId, Long cartItemId, CartItemUpdateQuantityDTO cartItemQuantityDTO) {
        Cart cart = findCart(customerId);
        CartItem cartItem = findCartItem(cart, cartItemId);

        validateVinylRecordStock(cartItem.getVinylRecord(), cartItemQuantityDTO.getNewQuantity());

        cartItem.setQuantity(cartItemQuantityDTO.getNewQuantity());
        cart.setUpdatedAt(LocalDateTime.now());

        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toResponseDTO(savedCart);
    }

    public CartResponseDTO removeCartItemFromCart(Long customerId, Long cartItemId) {
        Cart cart = findCart(customerId);
        CartItem cartItem = findCartItem(cart, cartItemId);

        cart.getCartItems().remove(cartItem);
        cart.setUpdatedAt(LocalDateTime.now());

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

    private Cart findOrCreateCart(Long customerId) {
        return cartRepository.findByCustomerId(customerId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setCustomer(findCustomer(customerId));
                    newCart.setCreatedAt(LocalDateTime.now());
                    return cartRepository.save(newCart);
                });
    }

    private CartItem findCartItem(Cart cart, Long cartItemId) {
        return cart.getCartItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new RecordNotFoundException("Cart item with id " + cartItemId + " not found in this cart"));
    }

    private void validateCartItemNotInCart(Cart cart, VinylRecord vinylRecord) {
        boolean alreadyInCart = cart.getCartItems().stream()
                .anyMatch(item -> item.getVinylRecord().equals(vinylRecord));

        if (alreadyInCart) {
            throw new ConflictException("The vinyl record '" + vinylRecord.getTitle() + "' is already in your cart." +
                    "You can update the quantity of this item." );
        }
    }

    private void validateVinylRecordStock(VinylRecord vinylRecord, Integer quantity) {
        Integer amountInStock = vinylRecord.getStock().getAmountInStock();

        if (amountInStock == 0) {
            throw new InsufficientStockException("Vinyl record " + vinylRecord.getTitle() + " is sold out");
        }

        if (amountInStock < quantity) {
            throw new InsufficientStockException("Only " + amountInStock + " items left of '" + vinylRecord.getTitle() + "'");
        }
    }

}
