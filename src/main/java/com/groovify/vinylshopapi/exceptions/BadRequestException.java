package com.groovify.vinylshopapi.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException() {
        super("Invalid request. Please check your input and try again.");
    }

    public BadRequestException(String message) {
        super(message);
    }
}

