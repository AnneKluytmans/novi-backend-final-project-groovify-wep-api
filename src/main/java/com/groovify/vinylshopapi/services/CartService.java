package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.CartResponseDTO;
import com.groovify.vinylshopapi.exceptions.DeleteOperationException;
import com.groovify.vinylshopapi.exceptions.RecordNotFoundException;
import com.groovify.vinylshopapi.mappers.CartMapper;
import com.groovify.vinylshopapi.models.Cart;
import com.groovify.vinylshopapi.repositories.CartRepository;
import com.groovify.vinylshopapi.repositories.CustomerRepository;
import com.groovify.vinylshopapi.specifications.CartSpecification;
import com.groovify.vinylshopapi.utils.SortHelper;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final CustomerRepository customerRepository;

    public CartService(
            CartRepository cartRepository,
            CartMapper cartMapper,
            CustomerRepository customerRepository) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.customerRepository = customerRepository;
    }

    public List<CartResponseDTO> getAllCarts(
            String createdBefore,
            String createdAfter,
            String updatedBefore,
            String updatedAfter,
            Long customerId,
            Boolean isEmpty,
            String sortBy,
            String sortOrder
    ) {
        Sort sort = SortHelper.getSort(sortBy, sortOrder, List.of("createdAt", "updatedAt", "id"));
        Specification<Cart> specification = CartSpecification.filterCarts(
                createdBefore, createdAfter, updatedBefore, updatedAfter,
                customerId, isEmpty
        );
        List<Cart> carts = cartRepository.findAll(specification, sort);
        return cartMapper.toResponseDTOs(carts);
    }

    public CartResponseDTO getCartById(Long cartId) {
        return cartMapper.toResponseDTO(findCart(cartId));
    }

    public void deleteCart(Long cartId) {
        Cart cart = findCart(cartId);

        if (!cart.getCartItems().isEmpty()) {
            throw new DeleteOperationException("Cart is not empty and cannot be deleted");
        }

        cart.getCustomer().setCart(null);
        customerRepository.save(cart.getCustomer());
        cartRepository.delete(cart);
    }

    private Cart findCart(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new RecordNotFoundException("Cart with id " + cartId + " not found"));
    }
}
