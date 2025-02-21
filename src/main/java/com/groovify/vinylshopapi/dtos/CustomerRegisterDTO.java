package com.groovify.vinylshopapi.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerRegisterDTO extends UserResponseDTO {
    @NotNull(message = "Subscribe status is required")
    private boolean newsletterSubscribed = true;
}
