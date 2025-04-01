package com.groovify.vinylshopapi.exceptions;

public class DeleteOperationException extends RuntimeException {
    public DeleteOperationException() {
        super("You are not allowed to delete this resource");
    }

    public DeleteOperationException(String message) {
        super(message);
    }
}
