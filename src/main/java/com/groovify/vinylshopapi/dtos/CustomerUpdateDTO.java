package com.groovify.vinylshopapi.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerUpdateDTO extends UserUpdateDTO {
    @NotNull(message = "Newsletter subscribe status is required")
    private Boolean newsletterSubscribed;
}
