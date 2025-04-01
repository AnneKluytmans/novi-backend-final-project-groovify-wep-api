package com.groovify.vinylshopapi.exceptions;

public class InvalidFileTypeException extends RuntimeException {
    public InvalidFileTypeException() {
        super("The type of the provided file is invalid. Please try again.");
    }

    public InvalidFileTypeException(String message) {
        super(message);
    }
}
