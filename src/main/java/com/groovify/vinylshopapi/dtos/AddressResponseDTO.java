package com.groovify.vinylshopapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponseDTO {
    private Long id;
    private String street;
    private String houseNumber;
    private String city;
    private String postalCode;
    private String country;
    private Boolean isShippingAddress;
    private Boolean isBillingAddress;
}
