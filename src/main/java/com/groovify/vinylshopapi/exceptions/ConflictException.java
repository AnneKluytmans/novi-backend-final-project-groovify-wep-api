package com.groovify.vinylshopapi.exceptions;

public class ConflictException extends RuntimeException {
    public ConflictException() {
        super("A conflict occurred while processing your request.");
    }

    public ConflictException(String message) {
        super(message);
    }
}

