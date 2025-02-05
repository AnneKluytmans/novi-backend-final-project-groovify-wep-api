package com.groovify.vinylshopapi.exceptions;

public class ArtistDeleteException extends RuntimeException {
    public ArtistDeleteException() {
        super("Cannot delete artist with existing vinyl records");
    }

    public ArtistDeleteException(String message) {
        super(message);
    }
}
