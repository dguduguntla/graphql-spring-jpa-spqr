package com.gdp.graphql.exception;

/**
 * This exception is thrown if the input resoure already exists.
 */
public class InvalidInputDataException extends RuntimeException {
    public InvalidInputDataException(String message) {
        super(message);
    }
}