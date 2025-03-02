package com.groovify.vinylshopapi.exceptions;

public class DeactivatedException extends RuntimeException {
    private String email;

    public DeactivatedException(String message) {
        super(message);
    }

    public DeactivatedException(String message, String email) {
        super(message);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
