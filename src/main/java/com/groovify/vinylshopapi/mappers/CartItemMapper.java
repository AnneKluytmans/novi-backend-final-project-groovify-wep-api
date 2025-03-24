package com.groovify.vinylshopapi.mappers;

import com.groovify.vinylshopapi.dtos.CartItemRequestDTO;
import com.groovify.vinylshopapi.dtos.CartItemResponseDTO;
import com.groovify.vinylshopapi.models.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {VinylRecordMapper.class})
public interface CartItemMapper {
    @Mapping(target = "id", ignore = true)
    CartItem toEntity(CartItemRequestDTO cartItemRequestDTO);

    @Mapping(target = "vinylRecord", source = "vinylRecord")
    CartItemResponseDTO toResponseDTO(CartItem cartItem);

    List<CartItemResponseDTO> toResponseDTOs(List<CartItem> cartItems);
}
