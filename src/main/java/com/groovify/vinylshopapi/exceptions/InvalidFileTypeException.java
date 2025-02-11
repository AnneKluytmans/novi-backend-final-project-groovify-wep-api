package com.groovify.vinylshopapi.exceptions;

public class InvalidFileTypeException extends RuntimeException {
    public InvalidFileTypeException() {
        super("Invalid file type.");
    }

    public InvalidFileTypeException(String message) {
        super(message);
    }
}
