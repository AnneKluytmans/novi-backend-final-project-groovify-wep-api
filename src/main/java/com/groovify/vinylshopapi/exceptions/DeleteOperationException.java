package com.groovify.vinylshopapi.exceptions;

public class DeleteOperationException extends RuntimeException {
    public DeleteOperationException(String entity, String relatedEntity) {
        super("Cannot delete " + entity + " because it is still linked to existing " + relatedEntity);
    }

    public DeleteOperationException(String message) {
        super(message);
    }
}
