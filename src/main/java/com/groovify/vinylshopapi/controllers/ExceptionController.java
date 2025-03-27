package com.groovify.vinylshopapi.controllers;

import com.groovify.vinylshopapi.exceptions.*;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionController {

    private ResponseEntity<Object> buildErrorResponse(HttpStatus status, String error, String message, Map<String, Object> extraFields) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        if (extraFields != null) {
            body.putAll(extraFields);
        }
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,"Bad Request", ex.getMessage(), null
        );
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST, "Illegal Argument", ex.getMessage(), null
        );
    }

    @ExceptionHandler(value = RecordNotFoundException.class)
    public ResponseEntity<Object> handleRecordNotFoundException(RecordNotFoundException ex) {
        return buildErrorResponse(
                HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), null
        );
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return buildErrorResponse(
                HttpStatus.NOT_FOUND, "Username not found", ex.getMessage(), null
        );
    }

    @ExceptionHandler(value = IndexOutOfBoundsException.class)
    public ResponseEntity<Object> handleIndexOutOfBoundsException(IndexOutOfBoundsException ex) {
        return buildErrorResponse(
                HttpStatus.NOT_FOUND, "Index Out Of Bounds", ex.getMessage(), null
        );
    }

    @ExceptionHandler(value = ConflictException.class)
    public ResponseEntity<Object> handleConflictException(ConflictException ex) {
        return buildErrorResponse(
                HttpStatus.CONFLICT, "Conflict", ex.getMessage(), null
        );
    }

    @ExceptionHandler(value = InsufficientStockException.class)
    public ResponseEntity<Object> handleInsufficientStockException(InsufficientStockException ex) {
        return buildErrorResponse(
                HttpStatus.CONFLICT, "Insufficient Stock", ex.getMessage(), null
        );
    }

    @ExceptionHandler(value = ForbiddenException.class)
    public ResponseEntity<Object> handleForbiddenException(ForbiddenException ex) {
        return buildErrorResponse(
                HttpStatus.FORBIDDEN, "Forbidden", ex.getMessage(), null
        );
    }

    @ExceptionHandler(value = DeleteOperationException.class)
    public ResponseEntity<Object> handleDeleteOperationException(DeleteOperationException ex) {
        return buildErrorResponse(
                HttpStatus.CONFLICT, "Delete Operation Conflict", ex.getMessage(), null
        );
    }

    @ExceptionHandler(value = InvalidOrderStatusException.class)
    public ResponseEntity<Object> handleInvalidOrderStatusException(InvalidOrderStatusException ex) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST, "Invalid Order Status", ex.getMessage(), null
        );
    }

    @ExceptionHandler(value = IOException.class)
    public ResponseEntity<Object> handleIOException(IOException ex) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST, "File Processing Error", ex.getMessage(), null
        );
    }

    @ExceptionHandler(value = InvalidFileTypeException.class)
    public ResponseEntity<Object> handleInvalidFileTypeException(InvalidFileTypeException ex) {
        return buildErrorResponse(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Invalid File Type", ex.getMessage(), null
        );
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Object> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        String message = "File size exceeded max upload size. Max upload size is " + ex.getMessage();
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST, "Max Upload Size Exceeded", message, null
        );
    }

    @ExceptionHandler(value = DeactivatedException.class)
    public ResponseEntity<Object> handleDeactivatedException(DeactivatedException ex) {
        Map<String, Object> extraFields = new HashMap<>();
        if(ex.getEmail() != null) {
            extraFields.put("email", ex.getEmail());
        }
        return buildErrorResponse(
                HttpStatus.CONFLICT, "Deactivated Conflict", ex.getMessage(), extraFields
        );
    }

    @ExceptionHandler(value = TeapotException.class)
    public ResponseEntity<Object> handleTeapotException(TeapotException ex) {
        return buildErrorResponse(
                HttpStatus.I_AM_A_TEAPOT,"I'm A Teapot", ex.getMessage(), null
        );
    }
}

