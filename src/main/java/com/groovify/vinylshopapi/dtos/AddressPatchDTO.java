package com.groovify.vinylshopapi.dtos;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressPatchDTO {
    @Size(max = 255, message = "Street cannot be longer than 255 characters")
    private String street;

    @Pattern(regexp = "^[0-9]+[A-Za-z]*$", message = "House number must be a number followed by an optional letter")
    private String houseNumber;

    @Size(max = 100, message = "City cannot be longer than 100 characters")
    private String city;

    @Pattern(regexp = "^[0-9]{4}[A-Za-z]{2}$", message = "Postal code must follow the pattern '1234AB'")
    private String postalCode;

    @Size(max = 100, message = "Country cannot be longer than 100 characters")
    private String country;

    private Boolean isShippingAddress;

    private Boolean isBillingAddress;
}
