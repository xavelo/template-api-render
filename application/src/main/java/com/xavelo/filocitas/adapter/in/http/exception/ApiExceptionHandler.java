package com.xavelo.filocitas.adapter.in.http.exception;

import com.xavelo.filocitas.application.exception.QuoteAlreadyExistsException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    private static final Logger logger = LogManager.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(QuoteAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleQuoteAlreadyExists(QuoteAlreadyExistsException exception) {
        logger.warn("Duplicate quote detected: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiErrorResponse.fromMessage(exception.getMessage()));
    }
}

