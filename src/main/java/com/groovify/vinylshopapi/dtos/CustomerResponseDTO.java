package com.groovify.vinylshopapi.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerResponseDTO extends UserResponseDTO {
    private Boolean newsletterSubscribed;
    private List<VinylRecordSummaryResponseDTO> favoriteVinylRecords;
    private List<AddressResponseDTO> addresses;
}
