package com.groovify.vinylshopapi.exceptions;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("The provided password is invalid. Please check the password and try again.");
    }

    public InvalidPasswordException(String message) {
        super(message);
    }
}
