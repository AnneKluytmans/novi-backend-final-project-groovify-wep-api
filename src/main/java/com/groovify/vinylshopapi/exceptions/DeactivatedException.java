package com.groovify.vinylshopapi.exceptions;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class DeactivatedException extends RuntimeException {
    private String email;

    public DeactivatedException(String message) {
        super(message);
    }

    public DeactivatedException(String message, String email) {
        super(message);
        this.email = email;
    }
}
