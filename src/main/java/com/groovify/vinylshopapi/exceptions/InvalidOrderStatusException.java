package com.groovify.vinylshopapi.exceptions;

public class InvalidOrderStatusException extends RuntimeException {
  public InvalidOrderStatusException() {
      super("Invalid order status");
  }

  public InvalidOrderStatusException(String message) {
        super(message);
    }
}
