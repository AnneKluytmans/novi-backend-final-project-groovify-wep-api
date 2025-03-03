package com.groovify.vinylshopapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefaultAddressesResponseDTO {
    private AddressSummaryResponseDTO shippingAddress;
    private AddressSummaryResponseDTO billingAddress;
}
