package com.groovify.vinylshopapi.exceptions;

public class RecordNotFoundException extends RuntimeException {
    public RecordNotFoundException() {
        super("The requested record was not found.");
    }

    public RecordNotFoundException(String message) {
        super(message);
    }
}