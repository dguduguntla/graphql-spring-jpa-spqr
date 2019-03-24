package com.gdp.graphql.exception;

/**
 * This exception is thrown if the input resoure already exists.
 */
public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}