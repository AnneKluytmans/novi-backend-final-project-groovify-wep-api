package com.groovify.vinylshopapi.exceptions;

public class InvalidVerificationException extends RuntimeException {
    public InvalidVerificationException() {
        super("Invalid verification code.");
    }

    public InvalidVerificationException(String message) {
        super(message);
    }
}
