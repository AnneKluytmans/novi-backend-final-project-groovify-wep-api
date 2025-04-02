package com.groovify.vinylshopapi.exceptions;

public class InvalidOrderStatusException extends RuntimeException {
  public InvalidOrderStatusException() {
      super("The order status in the request is invalid. Please check the status and try again.");
  }

  public InvalidOrderStatusException(String message) {
        super(message);
    }
}
