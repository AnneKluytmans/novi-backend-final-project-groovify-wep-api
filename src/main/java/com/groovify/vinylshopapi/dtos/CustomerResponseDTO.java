package com.groovify.vinylshopapi.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerResponseDTO extends UserResponseDTO {
    private Set<Long> favoriteVinylRecords;
}
