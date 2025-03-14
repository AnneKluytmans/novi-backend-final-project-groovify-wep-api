package com.groovify.vinylshopapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDTO {
    private Long id;
    private List<CartItemResponseDTO> cartItems;
}
