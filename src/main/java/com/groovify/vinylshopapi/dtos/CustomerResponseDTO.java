package com.groovify.vinylshopapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CustomerResponseDTO extends UserResponseDTO {
    private Boolean newsletterSubscribed;
    private List<VinylRecordSummaryResponseDTO> favoriteVinylRecords;
}
