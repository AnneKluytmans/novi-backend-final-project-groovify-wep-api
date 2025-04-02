package com.groovify.vinylshopapi.exceptions;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException() {
        super("You are not allowed to perform this operation");
    }

    public ForbiddenException(String message) {
        super(message);
    }
}
