// exception/ResourceNotFoundException.java
package com.libraryapp.backend.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}