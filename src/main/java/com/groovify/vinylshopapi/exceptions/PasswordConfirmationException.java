package com.groovify.vinylshopapi.exceptions;

public class PasswordConfirmationException extends RuntimeException {
    public PasswordConfirmationException() {
        super("The new password and confirmation password in the request do not match. Please try again.");
    }
    public PasswordConfirmationException(String message) {
        super(message);
    }
}
