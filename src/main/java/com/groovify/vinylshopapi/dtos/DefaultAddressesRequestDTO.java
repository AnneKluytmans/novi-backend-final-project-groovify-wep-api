package com.groovify.vinylshopapi.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefaultAddressesRequestDTO {
    @NotNull(message = "Is shipping status is required")
    private Boolean isShippingAddress;

    @NotNull(message = "Is billing status is required")
    private Boolean isBillingAddress;
}
