package com.groovify.vinylshopapi.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerPatchDTO extends UserPatchDTO {
    private Boolean newsletterSubscribed;
}
