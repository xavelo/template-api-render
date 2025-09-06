package com.xavelo.template.render.api.application.exception;

import java.util.UUID;

/**
 * Exception thrown when a user cannot be found.
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(UUID userId) {
        super("User " + userId + " not found");
    }
}

