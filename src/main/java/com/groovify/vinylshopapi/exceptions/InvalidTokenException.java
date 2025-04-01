package com.groovify.vinylshopapi.exceptions;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super("Token is expired or invalid");
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}
