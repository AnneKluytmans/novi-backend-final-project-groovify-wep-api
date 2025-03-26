package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.CartResponseDTO;
import com.groovify.vinylshopapi.exceptions.DeleteOperationException;
import com.groovify.vinylshopapi.exceptions.RecordNotFoundException;
import com.groovify.vinylshopapi.mappers.CartMapper;
import com.groovify.vinylshopapi.models.Cart;
import com.groovify.vinylshopapi.repositories.CartRepository;
import com.groovify.vinylshopapi.specifications.CartSpecification;
import com.groovify.vinylshopapi.utils.SortHelper;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    public CartService(
            CartRepository cartRepository,
            CartMapper cartMapper
    ) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
    }

    public List<CartResponseDTO> getCarts(
            LocalDate createdBefore,
            LocalDate createdAfter,
            LocalDate updatedBefore,
            LocalDate updatedAfter,
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
        return cartMapper.toResponseDTOs(cartRepository.findAll(specification, sort));
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
        cartRepository.save(cart);
        cartRepository.delete(cart);
    }

    private Cart findCart(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new RecordNotFoundException("No cart found with id: " + cartId));
    }
}
