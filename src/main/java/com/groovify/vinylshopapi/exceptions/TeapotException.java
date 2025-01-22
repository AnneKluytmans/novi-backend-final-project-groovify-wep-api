package com.groovify.vinylshopapi.exceptions;

public class TeapotException extends RuntimeException {
    public TeapotException() {
        super("I'm a teapot and cannot brew coffee.");
    }

    public TeapotException(String message) {
        super(message);
    }
}

