package com.groovify.vinylshopapi.exceptions;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super("The provided token is either expired or invalid. Please provide a valid token.");
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}
