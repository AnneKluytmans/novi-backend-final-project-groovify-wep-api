package com.groovify.vinylshopapi.exceptions;

public class PasswordConfirmationException extends RuntimeException {
    public PasswordConfirmationException() {
        super("New password and confirm password do not match");
    }
    public PasswordConfirmationException(String message) {
        super(message);
    }
}
