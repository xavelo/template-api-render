package com.xavelo.filocitas.application.exception;

/**
 * Exception thrown when attempting to persist a quote that already exists in the system.
 */
public class QuoteAlreadyExistsException extends RuntimeException {

    public QuoteAlreadyExistsException(String message) {
        super(message);
    }

    public QuoteAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}

