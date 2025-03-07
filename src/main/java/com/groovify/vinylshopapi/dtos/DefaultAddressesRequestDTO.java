package com.groovify.vinylshopapi.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DefaultAddressesRequestDTO {
    @NotNull(message = "Is shipping status is required")
    private Boolean isShippingAddress;

    @NotNull(message = "Is billing status is required")
    private Boolean isBillingAddress;
}
