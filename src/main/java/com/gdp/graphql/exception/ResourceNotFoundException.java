package com.gdp.graphql.exception;

/**
 * This exception is thrown when any entity is not found.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}