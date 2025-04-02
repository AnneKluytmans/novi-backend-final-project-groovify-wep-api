package com.groovify.vinylshopapi.exceptions;

public class MissingTokenException extends RuntimeException {
  public MissingTokenException() {
    super("Authentication token is missing. Please provide a valid token.");
  }

  public MissingTokenException(String message) {
        super(message);
    }
}
