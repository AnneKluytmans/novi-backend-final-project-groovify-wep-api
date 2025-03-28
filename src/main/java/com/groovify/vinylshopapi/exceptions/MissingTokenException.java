package com.groovify.vinylshopapi.exceptions;

public class MissingTokenException extends RuntimeException {
  public MissingTokenException() {
    super("Token is missing");
  }

  public MissingTokenException(String message) {
        super(message);
    }
}
