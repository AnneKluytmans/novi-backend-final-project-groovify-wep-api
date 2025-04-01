package com.groovify.vinylshopapi.exceptions;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException() {
        super("There is not enough stock available to fulfill your request.");
    }

    public InsufficientStockException(String message) {
        super(message);
    }
}
