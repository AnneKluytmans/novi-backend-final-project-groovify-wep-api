package com.groovify.vinylshopapi.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerRegisterDTO extends UserRegisterDTO {
    @NotNull(message = "Subscribe status is required")
    private boolean newsletterSubscribed = true;
}
