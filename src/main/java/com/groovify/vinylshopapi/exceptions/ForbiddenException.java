package com.groovify.vinylshopapi.exceptions;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException() {
        super("Forbidden exception");
    }

    public ForbiddenException(String message) {
        super(message);
    }
}
