package com.groovify.vinylshopapi.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequestDTO {
    @NotBlank(message = "Street is required")
    @Size(max = 255, message = "Street cannot be longer than 255 characters")
    private String street;

    @NotBlank(message = "House number is required")
    @Pattern(regexp = "^[0-9]+[A-Za-z]*$", message = "House number must be a number followed by an optional letter")
    private String houseNumber;

    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City cannot be longer than 100 characters")
    private String city;

    @NotBlank(message = "Postal code is required")
    @Pattern(regexp = "^(?:[0-9]{4}[A-Za-z]{2}|[0-9]{4})$", message = "Postal code must follow the pattern '1234AB' (NL) or '1234' (BE)")
    private String postalCode;

    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country cannot be longer than 100 characters")
    private String country;

    private Boolean isShippingAddress;

    private Boolean isBillingAddress;
}
