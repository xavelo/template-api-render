package com.xavelo.filocitas.adapter.in.http.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xavelo.filocitas.application.exception.DuplicatedQuoteException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = AdminController.class)
public class AdminExceptionHandler {

    private static final Logger logger = LogManager.getLogger(AdminExceptionHandler.class);

    private final ObjectMapper objectMapper;

    public AdminExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler(DuplicatedQuoteException.class)
    public ResponseEntity<JsonNode> handleDuplicatedQuote(DuplicatedQuoteException exception) {
        var payload = exception.getPayload();
        logger.warn("Duplicated quote received. Echoing payload to caller.", exception);
        if (payload == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        try {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(objectMapper.readTree(payload));
        } catch (JsonProcessingException jsonProcessingException) {
            logger.warn("Unable to deserialize duplicated quote payload, returning raw content.", jsonProcessingException);
            var fallbackNode = objectMapper.createObjectNode();
            fallbackNode.put("payload", payload);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(fallbackNode);
        }
    }
}

