package com.groovify.vinylshopapi.exceptions;

public class DeactivedException extends RuntimeException {
    private String email;

    public DeactivedException(String message) {
        super(message);
    }

    public DeactivedException(String message, String email) {
        super(message);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
