package com.groovify.vinylshopapi.mappers;

import com.groovify.vinylshopapi.dtos.CartResponseDTO;
import com.groovify.vinylshopapi.models.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = CartItemMapper.class)
public interface CartMapper {
    @Mapping(target = "cartItems", source = "cartItems")
    CartResponseDTO toCartResponseDTO(Cart cart);

    List<CartResponseDTO> toCartResponseDTOs(List<Cart> carts);
}
